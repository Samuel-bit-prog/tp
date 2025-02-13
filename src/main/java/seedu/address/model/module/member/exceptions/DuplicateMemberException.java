package seedu.address.model.module.member.exceptions;

/**
 * Signals that the operation will result in duplicate Members (Members are considered duplicates if they have the same
 * identity).
 */
public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException() {
        super("Operation would result in duplicate members");
    }
}
