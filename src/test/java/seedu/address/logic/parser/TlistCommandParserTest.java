package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MEMBER_ID_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MEMBER_ID_DESC_ONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MEMBER_ID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TlistCommand;

class TlistCommandParserTest {
    private TlistCommandParser parser = new TlistCommandParser();

    @Test
    void parse_allFieldsPresent_success() {
        Index expectedMemberID = Index.fromOneBased(VALID_MEMBER_ID);

        assertParseSuccess(parser, MEMBER_ID_DESC_ONE,
                new TlistCommand(expectedMemberID));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TlistCommand.MESSAGE_USAGE);

        //missing member id
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid member id
        assertParseFailure(parser, INVALID_MEMBER_ID_DESC, MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_preambleMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TlistCommand.MESSAGE_USAGE);
        assertParseFailure(parser, Integer.toString(VALID_MEMBER_ID), expectedMessage);
    }
}
