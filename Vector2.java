public class Vector2 {
    public double x;
    public double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 scale(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public void addSelf(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }
    
    public void clear() {
        this.x = 0;
        this.y = 0;
    }
    public double magnitude(){
        return Math.sqrt(this.x*this.x + this.y*this.y);
    }
}