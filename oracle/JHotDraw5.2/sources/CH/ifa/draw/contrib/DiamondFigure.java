/*
 * Hacked together by Doug lea
 * Tue Feb 25 17:39:44 1997  Doug Lea  (dl at gee)
 *
 */

package CH.ifa.draw.contrib;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import CH.ifa.draw.figures.RectangleFigure;

/**
 * A diamond with vertices at the midpoints of its enclosing rectangle
 */
public  class DiamondFigure extends RectangleFigure {

  public DiamondFigure() {
    super(new Point(0,0), new Point(0,0));
  }

  public DiamondFigure(Point origin, Point corner) {
    super(origin,corner);
  }

  /** Return the polygon describing the diamond **/
  protected Polygon polygon() {
    Rectangle r = displayBox();
    Polygon p = new Polygon();
    p.addPoint(r.x, r.y+r.height/2);
    p.addPoint(r.x+r.width/2, r.y);
    p.addPoint(r.x+r.width, r.y+r.height/2);
    p.addPoint(r.x+r.width/2, r.y+r.height);
    return p;
  }

  public void draw(Graphics g) {
    Rectangle r = displayBox();
	Polygon p1 = new Polygon();
	p1.addPoint(r.x, r.y+r.height/2);
	p1.addPoint(r.x+r.width/2, r.y);
	p1.addPoint(r.x+r.width, r.y+r.height/2);
	p1.addPoint(r.x+r.width/2, r.y+r.height);
	Polygon p = p1;
    g.setColor(getFillColor());
    g.fillPolygon(p);
    g.setColor(getFrameColor());
    g.drawPolygon(p);
  }

  public Insets connectionInsets() {
    Rectangle r = displayBox();
    return new Insets(r.height/2, r.width/2, r.height/2, r.width/2);
  }

  public boolean containsPoint(int x, int y) {
    return polygon().contains(x, y);
  }

  /*public Point chop(Point p) {
    return PolygonFigure.chop(polygon(), p);
  }*/

}
