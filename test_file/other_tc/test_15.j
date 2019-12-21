class MainC {
    Void main () {
        Helper a;
        a = new Helper();
        a.t = 10;
        a = new Helper();
        a.t = 12;
        println(a.t);
    }
}

class Helper {
    Int t;
}