# EM-Assistant

Replication package for paper submission.
This repository contains an archive with IntelliJ IDEA plugin, installation and usage instructions.

### Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [OpenAI key configuration](#openai-key-configuration)
- [Plugin installation](#plugin-installation)
- [Usage example](#usage-example)
- [Datasets](#datasets)

## Overview
An IntelliJ IDEA plugin that recommends Extract Function refactoring based on the LLM's suggestions.

<img src="pictures/tool-overview.png" alt="Code Diff" width="600">

## Requirements
1. You need to install [IntelliJ IDEA](https://www.jetbrains.com/idea/download) 2023.1 or higher.
2. Configure OpenAI key.

## OpenAI key configuration
1. Sign up for OpenAI at https://beta.openai.com/signup.
2. Get your OpenAI API key.
3. Open IntelliJ IDEA, go to `Settings | Tools | Large Language Models` and enter your API key in the `OpenAI Key` field.

## Plugin installation
1. Download an archive with the plugin [here](plugin/llm-refactoring-plugin.zip).
2. Open IntelliJ IDEA, go to `Settings-> Plugins-> Install plugin from disk` and select `llm-refactoring-plugin.zip`.
3. Restart IntelliJ IDEA.

## Usage example
<img src="pictures/plugin-demo.gif" alt="Code Diff" width="800">

To use the plugin, you need to right-click on the method, select `Show Context Actions` and click on `Extract Function experiment`.
The plugin will then send a request to OpenAI and await a response. Once received, it will present the suggestions in a dialog box,
the corresponding code for extraction will be highlighted.
To choose one of the suggestions, just right-click on it, and the plugin will automatically apply extract function refactoring.

# Datasets
To validate our technique, we used the following datasets:

1. _Community Corpus-A_ consists of **122** Java methods and their corresponding Extract Method refactorings collected from
   five open-source repositories: MyWebMart, SelfPlanner, WikiDev, JHotDraw, and JUnit.
   This dataset previously served as the foundation for evaluating various state-of-the-art Extract Method refactoring
   automation tools, including JExtract, JDeodorant, SEMI, GEMS, and REMS.

2. _Community Corpus-B_ Silva et al. maintain an active corpus containing 448 Java methods, each accompanied by its
   respective extract method refactorings that open-source developers actually performed.Automatable changes represent “clean”
   commits in which the developers made a single type of change, i.e., performed Extract Method refactoring. We chose the
   latter category as our focus is on replicating the exact development scenarios where our tool only performs refactorings,
   it does not expand the code functionality. This resulted in **154** replicable refactorings.

3. _Extended Corpus_: To enhance the robustness of our evaluation with a sizable oracle of actual refactorings performed by
   developers, we constructed Extended Corpus. To create it, we employed RefactoringMiner for detecting _Extract Method_.
   We ran it on highly regarded open-source repositories: IntelliJ Community Edition, and CoreNLP. After filtering to remove
   refactoring commits that mixed feature additions (the one-liners and the extracted methods whose body overlapped a large
   proportion of the host method), we retain **2,849** _Extract Methods_ from these repositories.

## Dataset Details
The datasets included in this repository are represented in JSON format. We have used MongoDB to perform queries and
navigate through the data.

### Raw Data

#### Oracle
Each record has an attribute named _"oracle"_. For example:
```json
{
   ...
   "oracle": {
     "line_start": 612,
     "line_end": 630,
     "url": "https://github.com/JetBrains/intellij-community/tree/405abc6878abe05f755a5a0a349a880139b9163e/plugins/maven/src/test/java/org/jetbrains/idea/maven/dom/MavenFilteredPropertiesCompletionAndResolutionTest.java#L612-L630"
   },
   ...
}
```

The _"oracle"_ represents the _Extract Method_ refactoring that was performed by the developer. This oracle is used
throughout the evaluation process.

#### LLM raw data
Our study involved querying multiple LLMs with various temperatures. This LLM raw data was saved and further used for
answering RQ1, RQ2, and RQ3. This raw data can be found in the attribute _"llm_multishot_data"_.
For example:

```json
{
   ...
   "llm_multishot_data": {
      "temperature_1.0": [
         {
            "llm_raw_response": "{"id":"chatcmpl-7yUWi6uhbWG8V9CynMFe2mKW7crc6","object":"chat.completion","created":1694651380,"choices":[{"index":0,"message":{"role":"assistant","content":"[\n{\"function_name\":  \"extractMethod\", \"line_start\":  442, \"line_end\": 443}\n]"},"finish_reason":"stop"}],"usage":{"prompt_tokens":764,"completion_tokens":26,"total_tokens":790}}",
            "llm_processing_time": 1397,
            "shot_no": 1
         }
        ...
      ]
   }
}
```

#### Ranked candidates
Suggestions coming from LLMs are transformed into candidates by our tool. The data used to answer **RQ3: How effective is
EM-Assist in providing refactoring recommendations over existing approaches?**
we used the data stored in the _"jetgpt_ranking"_ property as shown in the following example:

```json
{
   ...
   "jetgpt_ranking": {
      "llm_multishot_data": {
         "temperature_1.0": {
            "rank_by_popularity_times_heat": [
               {
                  "candidate_type": "AS_IS",
                  "application_result": "OK",
                  "line_start": 612,
                  "line_end": 630,
                  ...
               }
            ],
            ...
         }
      }
   },
   ...
}
```

#### Suggestion evaluation
The data used to answer **RQ1: How effective are LLMs at generating refactoring suggestions?**, can be found in the
_"suggestion_evaluation"_ JSON attribute:

```json
{
   ...
   "suggestion_evaluation": {
      "llm_multishot_data": {
         "temperature_1.0": [
            {
               "candidate_type": "AS_IS",
               "application_result": "OK",
               "application_reason": "",
               ...
            }
         ]
      }
   },
   ...
}
```

#### LiveREF execution data
To further strengthen the validity of our results, we applied EM-Assist on the _Extended Corpus_ that includes 2,849 actual
refactorings from open-source projects. We applied the most recent tool LiveRef, to the same dataset. The raw data for
LiveREF results is stored in the _"liveref_analysis"_ JSON attribute:

```json
{
   ...
   "liveref_analysis": {
      "rank_by_size": [
         {
            "line_start": 274,
            "line_end": 276,
            "length": 3,
            ...
         }
      ]
   },
   ...
}
```

### Processed data
The raw data described above was further processed to obtain various statistical analysis data.

#### RQ1 processed data
LLM effectiveness: `datasets/evaluation/extended_corpus/tool_evaluation__extended_corpus_llm_effectiveness_RQ1.csv`

#### RQ3 processed data
Our tool: `datasets/evaluation/extended_corpus/tool_evaluation__extended_corpus_ranking_RQ3.csv`

LiveRef: `datasets/evaluation/extended_corpus/tool_evaluation__extended_corpus_ranking_RQ3.csv`
