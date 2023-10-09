/*
 * Fri Feb 28 07:47:13 1997  Doug Lea  (dl at gee)
 * Based on PolyLineHandle
 */

package CH.ifa.draw.contrib;

import java.awt.Point;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.standard.AbstractHandle;


/**
 * A handle for a node on the polygon.
 */
public class PolygonHandle extends AbstractHandle {

  private int fIndex;
  private Locator fLocator;

  /**
   * Constructs a polygon handle.
   * @param owner the owning polygon figure.
   * @l the locator
   * @index the index of the node associated with this handle.
   */
  public PolygonHandle(PolygonFigure owner, Locator l, int index) {
    super(owner);
    fLocator = l;
    fIndex = index;
  }

  public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
    myOwner().setPointAt(new Point(x, y), fIndex);
  }

  public void invokeEnd  (int x, int y, int anchorX, int anchorY, DrawingView view) {
    myOwner().smoothPoints();
  }

  public Point locate() {
    return fLocator.locate(owner());
  }

  private PolygonFigure myOwner() {
    return (PolygonFigure)owner();
  }
}


