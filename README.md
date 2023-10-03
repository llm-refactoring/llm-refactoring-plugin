# EM-Assistant

Replication package for paper submission.
This repository contains an archive with IntelliJ IDEA plugin, installation and usage instructions.

### Table of Contents

- [Overview](#overview)
- [Requirements](#requirements)
- [OpenAI key configuration](#openai-key-configuration)
- [Plugin installation](#plugin-installation)
- [Usage example](#usage-example)

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
