class Main {
    Void main() {
        return;
    }
}

class AnotherMain {
    Helper abc;
    Main t;
    Int main(Int x) {
        AnotherMain o;
        String a;
        Int y;
        Bool z;
        a = "asd";
        o = new AnotherMain();
        t = new Main();
        o = this;
        o.abc.x = y;
        y = 3 * o.abc.x;
        z = y == 3;
        println("Square of d larger than sum of squares");
        println(y);
        println(z);
        readln(y);
        readln(a);
        readln(z);
        main(10);
        t.main();
        while(10 < y) {
            t.main();
        }
        if (10 < y) {
            t.main();
        } else {
            t.main();
        }
        y = new Helper().f().f();
        return x + 2 + y;
    }
}
class Helper {
    Int x;
    Another f() {
        return new Another();
    }
}

class Another {
    Int f() {
        Int val;
        val = -10;
        while(val < 0) {
            val = val + 1 + 1 + 1;
        }
        return val;
    }

    Int g() {
        f();
        return f() + f();
    }

    Int h(Int a, Int b, Int f) {
        println(f);
    }
}


class Test {
    Int a;
    Int b;
    Int f(){
            return 3;
    }
}