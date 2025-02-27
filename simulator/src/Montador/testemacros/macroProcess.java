package Montador.testemacros;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner; 

public class macroProcess {
    private macroProcess() {
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }
    public static void executeMacro() {
        LinkedList<Macro> macros = new LinkedList<Macro>();
        LinkedList<Instructions> instrucoes = new LinkedList<Instructions>();
        LinkedList<String> after = new LinkedList<String>();
        try {
            String mend = "MEND";
            String macroCheck = "MACRO";
            File myObj = new File("Macro/MACRO.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) { //verifica se há uma proxima linha
                String data = myReader.nextLine(); //passa a linha

                if (!data.isEmpty()) { //garante que não é uma linha vazia

                    macroCreator(data,myReader,macros,instrucoes,macroCheck,mend,after);

                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("Macro/MASMAPRG.ASM");
            String escrita = "";
            for (int x = 0; x < instrucoes.size(); x++) {
                if(x== instrucoes.size()-1){
                    escrita = escrita + instrucoes.get(x).getFullInstruction();
                }
                else escrita = escrita + instrucoes.get(x).getFullInstruction() + "\n";
            }
            if(after.size()!=0){
                escrita=escrita+"\n";
            }
            for(int aux=0;aux<after.size();aux++){
                if(aux==after.size()-1){
                    escrita = escrita + after.get(aux);
                }else escrita = escrita + after.get(aux) + "\n";
            }
            myWriter.write(escrita);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro.");
            e.printStackTrace();
        }

    }

    public static LinkedList<String> ArgumentsIdentifier(String data) {
        LinkedList<String> args = new LinkedList<>();
        int index = SecondSpaceIdentifier(data) + 1;
        int indexComma = SecondSpaceIdentifier(data) + 2;
        while (index != data.length() - 1) {
            if (!hasComment(data)) {
                if (data.charAt(index) == '&') {
                    while (data.charAt(indexComma) != ',' && indexComma != data.length() - 1) {
                        indexComma++;
                    }
                    if (indexComma == data.length() - 1) {
                        args.add(data.substring(index + 1));
                    } else {
                        args.add(data.substring(index + 1, indexComma));
                    }
                    indexComma++;
                }
                index++;
            } else {
                if (data.charAt(index) == '&') {
                    while (data.charAt(indexComma) != ',' && indexComma < FirstSemi(data)) {
                        indexComma++;
                    }
                    if (data.charAt(indexComma) == ';') {
                        args.add(data.substring(index + 1, FirstSemi(data)));
                    } else {
                        args.add(data.substring(index + 1, indexComma));
                    }
                    indexComma++;
                }
                index++;
            }
        }
        return args;
    }//att identifica todos argumentos(tanto da macro como do comano nativo(ex:ADD,MULT,etc)) e retorna em uma linked list

    public static int FirstSpaceIdentifier(String data) {
        int index = 0;
        while (data.charAt(index) != ' ') {
            index++;
        }
        return index;
    }//identifica o indice do primeiro espaço na string

    public static int SecondSpaceIdentifier(String data) {
        int index = FirstSpaceIdentifier(data) + 1;
        while (data.charAt(index) != ' ') {
            index++;
        }
        return index;
    }//identifica o indice do segundo espaço na string

    public static boolean hasComment(String data) {
        int cont = 0;
        while (cont < data.length() - 1) {
            cont++;
            if (data.charAt(cont) == ';') {
                return true;
            }
        }
        return false;
    }//retorna true ou false para o caso da string ter comentario

    public static int FirstSemi(String data) {
        int index = 0;
        while (data.charAt(index) != ';') {
            index++;
        }
        return index;
    }//retorna o indice do ponto e virgula

    public static boolean hasLabel(String data) {
        if (data.charAt(0) != ' ') {
            return true;
        }
        return false;
    }//retorna true ou false para o caso da string ter label

    public static boolean macroChecker(String data, LinkedList<Macro> macros) {
        int giro = 0;
        while (giro < macros.size()) {
            if (data.equals("STOP")) {
                return false;
            }
            if (data.substring(FirstSpaceIdentifier(data) + 1, SecondSpaceIdentifier(data)).equals(macros.get(giro).getMacroName())) {
                return true;
            }
            giro++;
        }
        return false;
    }//verifica se é apenas um opcode ou uma macro

    public static void instructionAdder(String data, LinkedList<Instructions> inst, LinkedList<String> after, Scanner myReader) {
        Instructions newInstruct;
        if (data.equals("STOP")) {
            newInstruct = new Instructions(data, "", "");
            inst.add(newInstruct);
            while(myReader.hasNextLine()){
                data=myReader.nextLine();
                after.add(data);
            }
        } else {
            String label = "";
            String comment = "";
            if (hasLabel(data)) {
                label = data.substring(0, FirstSpaceIdentifier(data));
            }
            if (hasComment(data)) {
                comment = data.substring(FirstSemi(data) + 1);
            }
            int var = FirstSpaceIdentifier(data);
            newInstruct = new Instructions(data.substring(FirstSpaceIdentifier(data) + 1, SecondSpaceIdentifier(data)), label, comment);//incializando e colocando o nome da instrucao

            if (newInstruct.getInstruct().equals("COPY")) {//ok
                int indexComma = var;//ok
                while (data.charAt(indexComma) != ',') { //Achar onde ta a virgula
                    indexComma++; //ok
                }
                String arg1 = data.substring(SecondSpaceIdentifier(data) + 1, indexComma); //Coloca o primeiro argumento do do segundo espaço ate a ,
                if (hasComment(data)) {
                    String arg2 = data.substring(indexComma + 1, FirstSemi(data));
                    newInstruct.setArg(arg1);
                    newInstruct.setArg(arg2);
                } else {
                    String arg2 = data.substring(indexComma + 1);//Coloca o primeiro argumento da , ate o fim

                    newInstruct.setArg(arg1);
                    newInstruct.setArg(arg2);
                }

            } else if (newInstruct.getInstruct().equals("STOP")) {
                newInstruct.setArg(null);
            } else {
                if (hasComment(data)) {
                    String arg = data.substring(SecondSpaceIdentifier(data) + 1, FirstSemi(data)); // Coloca o argumento do segundo espaço ate o ;
                    newInstruct.setArg(arg);
                } else {
                    String arg = data.substring(SecondSpaceIdentifier(data) + 1);// Coloca o argumento do segundo espaço ate o fim
                    newInstruct.setArg(arg);
                }
            }
            inst.add(newInstruct);
        }
    }

    public static void macroDecoder(String data, LinkedList<Macro> macros, LinkedList<Instructions> inst) {
        int cont = 0;
        int firstLimit = SecondSpaceIdentifier(data);
        int indexComma = 0;
        String instruct = data.substring(FirstSpaceIdentifier(data) + 1, SecondSpaceIdentifier(data));
        LinkedList<String> args = new LinkedList<>();
        while (cont < macros.size()) {//esse while pega os valor que tem nos macros, depois idsso vai ir para a parte de decodificar de fato (como faz pra ficar verdinho assim?)

            if (macros.get(cont).getMacroName().equals(instruct)) {

                while (firstLimit != data.length() - 1) {
                    while (data.charAt(indexComma) != ',' && indexComma < data.length() - 1) {
                        indexComma++;
                    }
                    if (indexComma == data.length() - 1 && hasComment(data)) {
                        args.add(data.substring(firstLimit + 1, FirstSemi(data)));
                    } else if (indexComma == data.length() - 1 && !hasComment(data)) {
                        args.add(data.substring(firstLimit + 1));
                    } else {
                        args.add(data.substring(firstLimit + 1, indexComma));
                    }
                    firstLimit = indexComma;
                    indexComma++;
                }
                break;
            }
            cont++;
        }//Ta passando tudo de dentro da macro pra lista de instrucoes
        for (int i = 0; i < macros.get(cont).commandSize(); i++) { //Decodifica a macro para o modo de instrucoes normal
            String pal = macros.get(cont).getCommandName(i);
            String label = macros.get(cont).getLabelName(i);
            if (label != null && !label.trim().isEmpty()) { //modifiquei porque nao reconhecia o .isBlank como função nativa do tipo String
                label = data.substring(0, FirstSpaceIdentifier(data));
            }

            String comment = macros.get(cont).getCommentName(i);
            Instructions newInstruct = new Instructions(pal, label, comment);
            newInstruct.setComment(comment);
            newInstruct.setLabel(label);

            if (pal.equals("COPY")) {
                int argNum1 = macros.get(cont).getCommandArg(i, 0);
                int argNum2 = macros.get(cont).getCommandArg(i, 1);
                String arg1 = args.get(argNum1);
                String arg2 = args.get(argNum2);

                newInstruct.setArg(arg1);
                newInstruct.setArg(arg2);
            } else if (pal.equals("STOP")) {
                newInstruct.setArg(null);
            } else {
                int argNum = macros.get(cont).getCommandArg(i, 0);
                String arg = args.get(argNum);
                newInstruct.setArg(arg);
            }
            inst.add(newInstruct);
        }
    }

    public static void macroCreator(String data, Scanner myReader,LinkedList<Macro> macros,LinkedList<Instructions> instrucoes, String macroCheck, String mend, LinkedList<String> after){
        if (data.contains(macroCheck)) { //Ve se e igual a palavra MACRO
            data = myReader.nextLine(); //passa a linha de novo
            Macro algo;
            String macroID = data.substring(FirstSpaceIdentifier(data) + 1, SecondSpaceIdentifier(data));//nome da macro está entre o primeiro e segundo espaço obrigatoriamente
            if (hasLabel(data) && hasComment(data)) {//tem label e comment
                algo = new Macro(macroID, data.substring(0, FirstSpaceIdentifier(data)), data.substring(FirstSemi(data) + 1));
            } else if (hasLabel(data) && !hasComment(data)) {//tem label e n tem comment
                algo = new Macro(macroID, data.substring(0, FirstSpaceIdentifier(data)), "");
            } else if (!hasLabel(data) && !hasComment(data)) {//n tem label e n tem comment
                algo = new Macro(macroID, "", "");
            } else {//n tem label e tem comment
                algo = new Macro(macroID, "", data.substring(FirstSemi(data) + 1));
            }
            algo.setArguments(ArgumentsIdentifier(data));//define o nome,label e comment
            data = myReader.nextLine();

            while (!data.equals(mend) ) {// laço para circular os comandos da macro objetivo agora é identificar qual o indice das chamadas dos comandos
                for(int i=0;i<macros.size();i++){//verifica se esta chamando uma macro ja criada, se estiver expande ela dentro da macro que esta sendo criada
                    if(data.contains(macros.get(i).getMacroName())){
                        addCommandsToMacro(algo,macros.get(i));
                        data = myReader.nextLine();
                    }
                }
                if(data.contains(macroCheck)){
                    macroCreator(data,myReader,macros,instrucoes,macroCheck,mend,after);
                    Macro filhote = macros.peekLast();
                    addCommandsToMacro(algo,filhote);
                    data = myReader.nextLine();
                }else{
                    String opcode = data.substring(FirstSpaceIdentifier(data) + 1, SecondSpaceIdentifier(data));
                    String label = "";
                    String comment = "";
                    if (hasLabel(data)) {
                        label = data.substring(0, FirstSpaceIdentifier(data) + 1);
                    }
                    if (hasComment(data)) {
                        comment = data.substring(FirstSemi(data) + 1);
                    }
                    Command code = new Command(opcode, label, comment); //comando é buildado e recebe a string do comando daquela linha
                    LinkedList<String> commandArgs = ArgumentsIdentifier(data); //commandArgs recebe os argumentos do comando
                    int arguments = 0;
                    while (arguments < commandArgs.size()) {//faz um loop para passar por todos argumentos do código
                        String argument = commandArgs.get(arguments);
                        int argIndex = 0;
                        while (argIndex < algo.getArguments().size()) {
                            if (algo.getArguments().get(argIndex).equals(argument)) {
                                code.setArgument(argIndex);
                            }
                            argIndex++;
                        }
                        arguments++;
                    }
                    algo.addCommand(code);
                    data = myReader.nextLine();

                }

            }
            macros.add(algo);
        } else {
            if (macroChecker(data, macros)) {
                macroDecoder(data, macros, instrucoes);
            } else {
                instructionAdder(data, instrucoes, after, myReader);
            }

        }
    }

    public static void addCommandsToMacro(Macro parent, Macro child){
        int tamchild = child.commandSize();
        int i = 0;
        while(i != tamchild){
            parent.addCommand(child.getCommand(i));
            i++;
        }
    }
}