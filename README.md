# CollisionManager
Collision Manager for checking collisions between added Objects(View) and Listeners

Need to extend View to allocate an Object for collision checks.
Also need to use Vector2 for x,y positions, velocity , etc.

### Collision Types
rect and rect collision -> rectCollision()
rect and circle collision -> rectCircleCollision() 
rect and point collision -> rectPointCollision()
circle and circle collision -> circleCollision()
circle and point collision -> circlePointCollision()

### Enum
 - Direction
	 -  for rect and circle collision (8 directions + None)
 - Shape
	 - None, Rect, Circle, Point
