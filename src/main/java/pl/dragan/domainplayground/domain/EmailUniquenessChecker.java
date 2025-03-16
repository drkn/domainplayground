package pl.dragan.domainplayground.domain;

public interface EmailUniquenessChecker {
    boolean isUnique(Email email);
}
