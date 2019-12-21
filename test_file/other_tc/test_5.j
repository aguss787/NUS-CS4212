class MainC {
    Void main () {
        Helper h;
        h = new Helper();
        h.val = 787;
        h.g();
        h.g();
    }
}

class Helper {
    Int val;
    Void g() {
        println(this.val);
        this.val = 4;
    }
}