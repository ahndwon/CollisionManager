import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollisionManager {
    private static HashMap<Integer, List<View>> library = new HashMap<>();
    private static HashMap<View, OnCollisionListener> listeners = new HashMap<>();

    public enum Direction {
        None, LT, T, RT, R, RB, B, LB, L
    }

    public enum Shape {
        None, Rect, Circle, Point
    }

    public CollisionManager() {
    }

    public void addOnCollisionListener(View view, OnCollisionListener listener) {
        listeners.put(view, listener);
    }

    public static boolean rectCollision(Vector2 rect1, Vector2 rect2, int rect1Width, int rect1Height, int rect2Width, int rect2Height) {
        return (rect1.getX() - rect1Width / 2 <= rect2.getX() + rect2Width / 2
                && rect1.getX() + rect1Width / 2 >= rect2.getX() - rect2Width / 2
                && rect1.getY() - rect1Height / 2 <= rect2.getY() + rect2Height / 2
                && rect1.getY() + rect1Height / 2 >= rect2.getY() - rect2Height / 2);
    }

    private static Direction getCollisionDirection(View rect, View circle) {
        if ((Math.abs(rect.getPos().getX() - circle.getPos().getX()) > (rect.getWidth() / 2)
                && Math.abs(rect.getPos().getY() - circle.getPos().getY()) > (rect.getHeight() / 2))) {
            if (circle.getPos().getX() < rect.getPos().getX() && circle.getPos().getY() > rect.getPos().getY())
                return Direction.LT;
            else if (circle.getPos().getX() < rect.getPos().getX() && circle.getPos().getY() < rect.getPos().getY())
                return Direction.LB;
            else if (circle.getPos().getX() > rect.getPos().getX() && circle.getPos().getY() > rect.getPos().getY())
                return Direction.RT;
            else
                return Direction.RB;
        } else {
            if (circle.getPos().getX() < rect.getPos().getX())
                return Direction.L;
            else if (circle.getPos().getX() > rect.getPos().getX())
                return Direction.R;
            else if (circle.getPos().getY() < rect.getPos().getY())
                return Direction.B;
            else
                return Direction.T;
        }
    }

    public static boolean rectCircleCollision(Vector2 rect, Vector2 circle, int rectWidth, int rectHeight, int circleRadius) {
        if (circle.getX() > (rect.getX() - (rectWidth / 2) - circleRadius)
                && circle.getX() < (rect.getX() + (rectWidth / 2) + circleRadius)
                && circle.getY() > (rect.getY() - (rectHeight / 2) - circleRadius)
                && circle.getY() < (rect.getY() + (rectHeight / 2) + circleRadius)) {
            if (rect.distance(rect.getX(), rect.getY()) > rectWidth / 2
                    && rect.distance(rect.getX(), rect.getY()) > rectHeight / 2) {
                if (circle.distance(rect.getX() - rectWidth / 2, rect.getY() - rectHeight / 2) < circleRadius
                        || circle.distance(rect.getX() + rectWidth / 2, rect.getY() - rectHeight / 2) < circleRadius
                        || circle.distance(rect.getX() - rectWidth / 2, rect.getY() + rectHeight / 2) < circleRadius
                        || circle.distance(rect.getX() + rectWidth / 2, rect.getY() + rectHeight / 2) < circleRadius)
                    return true;
                else return false;
            } else return true;
        } else return false;
    }

    public static boolean circleCollision(Vector2 circle1, Vector2 circle2, int circle1Radius, int circle2Radius) {
        return circle1.distance(circle2.getX(), circle2.getY()) <= circle1Radius + circle2Radius;
    }

    public static boolean rectPointCollision(Vector2 rect, Vector2 point, int rectWidth, int rectHeight) {
        return rect.getX() - rectWidth / 2 <= point.getX()
                && rect.getX() + rectWidth / 2 >= point.getX()
                && rect.getY() - rectHeight / 2 >= point.getY()
                && rect.getY() + rectHeight / 2 <= point.getY();
    }

    public static boolean circlePointCollision(Vector2 circle, Vector2 point, int circleRadius) {
        return circle.distance(point.getX(), point.getY()) <= circleRadius;
    }

    public static void allocateView(int layer, View view) {
        List<View> list;

        if (!library.containsKey(layer)) {
            list = new ArrayList<>();
            list.add(view);
            library.put(layer, list);
        } else {
            list = library.get(layer);
            list.add(view);
        }
    }

    public static void allocateViews(int layer, List<View> viewList) {
        if (!library.containsKey(layer)) {
            library.put(layer, viewList);
        } else {
            List<View> list = library.get(layer);
            list.addAll(viewList);
        }
    }

    public static HashMap<Integer, List<View>> getLibrary() {
        return library;
    }

    private static void alertListener(View target, View comparisonTarget, Direction direction) {
        if (listeners.get(target) != null) {
            listeners.get(target).onCollision(target, comparisonTarget, direction);
        }
        if (listeners.get(comparisonTarget) != null)
            listeners.get(comparisonTarget).onCollision(comparisonTarget, target, direction);
    }

    public static void checkCollisions() {
        ArrayList<Integer> keySet = new ArrayList<>(library.keySet());
        for (int i = 0; i < keySet.size(); i++) {
            Integer layer = keySet.get(i);
            ArrayList<Integer> comparisons = new ArrayList<>(keySet);
            comparisons.remove(keySet.get(i));

            for (Integer other : comparisons) {
                for (View target : library.get(layer)) {
                    for (View comparison : library.get(other)) {
                        if (target.getShape().equals(Shape.Rect) && comparison.getShape().equals(Shape.Circle)) {
                            if (rectCircleCollision(target.getPos(), comparison.getPos(), target.getWidth(),
                                    target.getHeight(), comparison.getHeight() / 2)) {
                                alertListener(target, comparison, getCollisionDirection(target, comparison));
                                System.out.println("rectCircleCollision");
                            }
                        } else if (target.getShape().equals(Shape.Rect) && comparison.getShape().equals(Shape.Rect)) {
                            if (rectCollision(target.getPos(), comparison.getPos(), target.getWidth(),
                                    target.getHeight(), comparison.getWidth(), comparison.getHeight())) {
                                alertListener(target, comparison, Direction.None);
                                System.out.println("rectCollision");
                            }
                        } else if (target.getShape().equals(Shape.Rect) && comparison.getShape().equals(Shape.Point)) {
                            if (rectPointCollision(target.getPos(), comparison.getPos(), target.getWidth(), target.getHeight())) {
                                alertListener(target, comparison, Direction.None);
                                System.out.println("rectPointCollision");
                            }
                        } else if (target.getShape().equals(Shape.Circle) && comparison.getShape().equals(Shape.Circle)) {
                            if (circleCollision(target.getPos(), comparison.getPos(), target.getWidth() / 2, target.getHeight() / 2)) {
                                alertListener(target, comparison, Direction.None);
                                System.out.println("CircleCollision");
                            }
                        } else if (target.getShape().equals(Shape.Circle) && comparison.getShape().equals(Shape.Point)) {
                            if (circlePointCollision(target.getPos(), comparison.getPos(), target.getWidth() / 2)) {
                                alertListener(target, comparison, Direction.None);
                                System.out.println("CirclePointCollision");
                            }
                        }
                    }
                }
            }
        }
    }
}