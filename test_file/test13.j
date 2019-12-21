class MainC {
    Void main (){
        if(true) {
            return;
        }
        if(false) {
            return false;
        }
        if("blah") {
            return;
        }
        if(1) {
            return;
        }
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
