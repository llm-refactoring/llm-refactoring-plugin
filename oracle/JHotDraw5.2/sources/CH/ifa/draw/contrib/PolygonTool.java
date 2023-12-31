/*
 * Fri Feb 28 07:47:05 1997  Doug Lea  (dl at gee)
 * Based on ScribbleTool
 */

package CH.ifa.draw.contrib;

import java.awt.Point;
import java.awt.event.MouseEvent;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.standard.AbstractTool;


/**
 */
public class PolygonTool extends AbstractTool {

  private PolygonFigure  fPolygon;
  private int            fLastX, fLastY;

  public PolygonTool(DrawingView view) {
    super(view);
  }

  public void activate() {
    super.activate();
    fPolygon = null;
  }

  public void deactivate() {
    super.deactivate();
    if (fPolygon != null) {
      fPolygon.smoothPoints();
      if (fPolygon.pointCount() < 3 ||
          fPolygon.size().width < 4 || fPolygon.size().height < 4)
        drawing().remove(fPolygon);
    }
    fPolygon = null;
  }

  private void addPoint(int x, int y) {
      if (fPolygon == null) {
          fPolygon = new PolygonFigure(x, y);
          view().add(fPolygon);
          fPolygon.addPoint(x, y);
      } else if (fLastX != x || fLastY != y)
          fPolygon.addPoint(x, y);

      fLastX = x;
      fLastY = y;
  }

  public void mouseDown(MouseEvent e, int x, int y) {
    // replace pts by actual event pts
    x = e.getX();
    y = e.getY();

    if (e.getClickCount() >= 2) {
        if (fPolygon != null) {
            fPolygon.smoothPoints();
            editor().toolDone();
        }
        fPolygon = null;

    } else {
        // use original event coordinates to avoid
        // supress that the scribble is constrained to
        // the grid
        int x1 = e.getX();
		int y1 = e.getY();
		if (fPolygon == null) {
		      fPolygon = new PolygonFigure(x1, y1);
		      view().add(fPolygon);
		      fPolygon.addPoint(x1, y1);
		  } else if (fLastX != x1 || fLastY != y1)
		      fPolygon.addPoint(x1, y1);
		
		  fLastX = x1;
		  fLastY = y1;
    }
  }

  public void mouseMove(MouseEvent e, int x, int y) {
    if (fPolygon != null) {
       if (fPolygon.pointCount() > 1) {
           fPolygon.setPointAt(new Point(x, y), fPolygon.pointCount()-1);
           view().checkDamage();
       }
    }
  }

  public void mouseDrag(MouseEvent e, int x, int y) {
    // replace pts by actual event pts
    x = e.getX();
    y = e.getY();
    addPoint(x, y);
  }


  public void mouseUp(MouseEvent e, int x, int y) {
  }

}
