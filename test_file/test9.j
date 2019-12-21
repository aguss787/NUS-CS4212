class MainC {
    Void main (){
        Functional fo ;
        Int i;
        Int j ;
        readln(i) ;
        if (i > 0) {
            fo = new Functional() ;
            fo.a = 2;
            j = fo.f(i) ;
            println(j) ;
        }
        else {
            println("Error") ;
        }
        return ;
    }
}
class Functional {
    Int a;
    Int f (Int b){
        return 3;
    }
}
class Test {
    Test f() {
        Test t;
        t = f().f().f().f().g().g().f().f();
    }
    Test g() {
        return new Test();
    }
}