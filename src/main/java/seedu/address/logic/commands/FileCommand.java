package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_LOAD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILE_SAVE;

import java.nio.file.Path;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a FileCommand which can load, save or append data to a file.
 */
public class FileCommand extends Command {
    /**
     * Represents the operation of the FileCommand.
     */
    public enum FileOperation {
        LOAD, SAVE
    }

    public static final String COMMAND_WORD = "file";
    public static final String MESSAGE_SUCCESS = "File operation successful: %1$s";
    public static final String MESSAGE_ERROR = "File operation failed: %1$s";
    public static final String MESSAGE_STRING_UNFORMATTED = """
            %s: Loads, saves or appends data to a file.
            Parameters:
            %s FILE_PATH
            %s FILE_PATH

            Example:
            file /load data.json
            file /save data.json
            """;

    public static final String MESSAGE_USAGE = String.format(MESSAGE_STRING_UNFORMATTED, COMMAND_WORD, PREFIX_FILE_LOAD,
            PREFIX_FILE_SAVE);

    private final FileOperation operation;

    private final String fileName;

    /**
     * Creates a FileCommand to load, save or append data to a file.
     */
    public FileCommand(FileOperation operation, String filePath) {
        this.operation = operation;
        this.fileName = filePath;
    }

    /**
     * Executes the FileCommand.
     *
     * @param model Model
     * @return CommandResult
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        switch (operation) {
        case LOAD:
            return load(model);
        case SAVE:
            return save(model);
        default:
            throw new CommandException(String.format(MESSAGE_ERROR, "Invalid file operation"));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FileCommand // instanceof handles nulls
                        && operation.equals(((FileCommand) other).operation)
                        && fileName.equals(((FileCommand) other).fileName)); // state check
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("operation", operation)
                .add("filePath", fileName)
                .toString();
    }

    /**
     * Returns the operation of the FileCommand.
     *
     * @return FileOperation
     */
    public FileOperation getOperation() {
        return operation;
    }

    /**
     * Returns the file path of the FileCommand.
     *
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Executes the load operation.
     *
     * @param model Model
     * @return CommandResult
     */
    public CommandResult load(Model model) {
        Boolean status = true;

        if (status == false) {
            return new CommandResult(String.format(MESSAGE_ERROR, "Failed to load data from " + fileName));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, "Loaded data from " + fileName));
    }

    /**
     * Changes the address book file path to the specified file path.
     *
     * @param model Model to save
     * @return CommandResult indicating the result of the save operation
     */
    public CommandResult save(Model model) {
        Boolean status = true;

        model.setAddressBookFilePath(Path.of("data", fileName));

        if (status == false) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, "Failed to save data to " + fileName));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, "Change saved file to " + fileName));
    }
}
