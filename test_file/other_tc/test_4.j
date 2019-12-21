class MainC {
    Void main () {
        Helper h;
        h = new Helper();
        h.val = h.g();
        h.f(11, "asd");
        //h.val = 100;
        // println(h.val);
        h.fl(111, 222, 333, 444, 555);
    }
}

class Helper {
    Int val;
    Void f(Int a, String b) {
        println("start of f");
        println(this.val);
        println(a);
        println(b);

        this.val = 123;
        //println(this.val);

    }

    Int g() {
        Int x;

        x = 787;
        return x;
    }

    Void fl(Int a, Int b, Int c, Int d, Int e) {
        println("start of fllllllllllllll");

        println(this.val);

        println(a);
        println(b);
        println(c);
        println(d);
        println(e);
    }
}
