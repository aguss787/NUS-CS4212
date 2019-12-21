class MainC {
    Void main () {
        Helper h;
        Int n;
        Int k;
        h = new Helper();
        println("asd");
        readln(n);
        readln(k);
        println(h.f(n, k));
        h.t = 10;
        println(h.t);
    }
}

class Helper {
    Int t;
    Int f(Int n, Int k) {
        if(k > n || k < 0) {
            return 0;
        } else {
            if(k == 0) {
                return 1;
            } else {
                return f(n - 1, k - 1) + f(n - 1, k);
            }
        }
    }
}