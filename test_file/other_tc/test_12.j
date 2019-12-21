class MainC {
    Void main () {
        Helper h;
        Int i;
        h = new Helper();
        i = 0;
        while(i <= 10) {
            println(h.f(i));
            i = i + 1;
        }
    }
}

class Helper {
    Int f(Int cur) {
        if(cur <= 1) {
            return cur;
        } else {
            return f(cur - 2) + f(cur - 1);
        }
    }
}