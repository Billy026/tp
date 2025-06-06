package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COURSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARTICIPATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Id;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ID, PREFIX_NEW_ID, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_COURSE, PREFIX_ATTENDANCE, PREFIX_PARTICIPATION,
                        PREFIX_GRADE, PREFIX_NOTE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ID, PREFIX_NEW_ID, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_COURSE, PREFIX_ATTENDANCE, PREFIX_PARTICIPATION, PREFIX_GRADE);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (!argMultimap.getValue(PREFIX_ID).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        Id id = ParserUtil.parseId(argMultimap.getValue(PREFIX_ID).get());

        if (argMultimap.getValue(PREFIX_NEW_ID).isPresent()) {
            editPersonDescriptor.setNewId(ParserUtil.parseId(argMultimap.getValue(PREFIX_NEW_ID).get()));
        }
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_COURSE).isPresent()) {
            editPersonDescriptor.setCourse(ParserUtil.parseCourse(argMultimap.getValue(PREFIX_COURSE).get()));
        }
        if (argMultimap.getValue(PREFIX_ATTENDANCE).isPresent()) {
            editPersonDescriptor.setAttendance(
                    ParserUtil.parseAttendance(argMultimap.getValue(PREFIX_ATTENDANCE).get()));
        }
        if (argMultimap.getValue(PREFIX_PARTICIPATION).isPresent()) {
            editPersonDescriptor.setParticipation(
                    ParserUtil.parseParticipation(argMultimap.getValue(PREFIX_PARTICIPATION).get()));
        }
        if (argMultimap.getValue(PREFIX_GRADE).isPresent()) {
            editPersonDescriptor.setGrade(ParserUtil.parseGrade(argMultimap.getValue(PREFIX_GRADE).get()));
        }
        parseNotesForEdit(argMultimap.getAllValues(PREFIX_NOTE)).ifPresent(editPersonDescriptor::setNotes);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(id, editPersonDescriptor);
    }

    private Optional<List<Note>> parseNotesForEdit(Collection<String> notes) throws ParseException {
        assert notes != null;

        if (notes.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> noteList = notes.size() == 1 && notes.contains("") ? Collections.emptySet() : notes;
        return Optional.of(ParserUtil.parseNotes(noteList));
    }
}
