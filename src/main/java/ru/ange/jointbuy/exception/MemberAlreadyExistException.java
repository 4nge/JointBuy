package ru.ange.jointbuy.exception;

public class MemberAlreadyExistException extends Exception {

    public MemberAlreadyExistException() {
        super();
    }

    public MemberAlreadyExistException(String msg) {
        super(msg);
    }

}
