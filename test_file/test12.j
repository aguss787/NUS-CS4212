class MainC {
    Void main (){
        Helper m;
        m.f().f().f().f().f().f().f().f().f().f().f().f().f().f();
        m = m.f().f().f().f().f().f().f().f().f().f().f().f().f().f();
        m.g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1).g(1);
        m.g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y).g(m.y);
        m.g(1).g(2).g(3).g(4).g(5).g(6).g(7);
        m.h.h.h.h.h.h.h.h.h.h.g(10);
    }
}

class Helper {
    Int y;
    Helper h;
    Helper f() {
        return new Helper();
    }

    Helper g(Int x) {
        return new Helper();
    }
}
