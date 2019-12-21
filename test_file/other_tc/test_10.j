class MainC {
    Void main () {
        Helper h;
        h = new Helper();
        h.f(1, "asd");
    }
}

class Helper {
    Int t;
    Helper a;
    //Helper b;
    //Helper c;
    Void f(Int y, String x) {
        this.t = 10;
        println(t);
        t = 11;
        println(this.t);
        a = this.newFunc();
        println(a.t);
        a.t = 3;
        println(a.t);
    }

    Helper newFunc() {
        return new Helper();
    }
}
