package eg.edu.alexu.csd.oop.draw.command;

public class ActionCommand {
    Command undo;
    Command redo;
    public ActionCommand(Command undo, Command redo){
        this.undo = undo;
        this.redo = redo;
    }

    public void undo(){
        undo.exec();
    }
    public void redo(){
        redo.exec();
    }
}
