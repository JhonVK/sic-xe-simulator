package Montador.testemacros;
import java.util.LinkedList;

public class Instructions {
    private String instruct;
    private String comment;
    private String label;
    private LinkedList<String> arg;

    public Instructions(String instruct, String label, String comment) {
        this.arg = new LinkedList<>();
        this.instruct = instruct;
        this.comment = comment;
        this.label = label;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setArg(String info) {
        arg.add(info);
    }

    public String getFullInstruction() {
        String string;
        if(this.comment.isEmpty()) {
            if(this.arg.size()==2) {
                string = this.label + " " + this.instruct + " " + this.arg.get(0) + " " + this.arg.get(1);
            }else if(this.arg.size()==1) {
                string = this.label + " " + this.instruct + " " + this.arg.get(0);
            }else{
                string = this.instruct;
            }
        }else{
            if(this.arg.size()==2) {
                string = this.label + " " + this.instruct + " " + this.arg.get(0) + " " + this.arg.get(1) + ";" + this.comment;
            }else if(this.arg.size()==1) {
                string = this.label + " " + this.instruct + " " + this.arg.get(0) + ";" + this.comment;
            }else{
                string = this.instruct;
            }
        }
        return string;
    }
}
