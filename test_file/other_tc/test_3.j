class MainC {
    Void main () {
        Helper h;
        h = new Helper();
        h.val = h.g();
        h.f(111, "asd");
    }
}

class Helper {
    Int val;
    Void f(Int a, String b) {
        println(this.val);
        println(a);
        println(b);
    }

    Int g() {
        Int x;

        x = 787;
        return x;
    }
}
