import Mem.Memoria;
import Regs.Registradores;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Montador.Macros.MacroProcessor; // Importação do MacroProcessor
import java.io.IOException;
import java.util.List;

public class App extends Application {

    private Memoria memoria;
    private Registradores registradores;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 🛠 Passo 1: Executar o processador de macros
        String inputFile = "codigoFonte.asm";
        String outputFile = "MASMAPRG.asm";

        try {
            List<String> expandedCode = MacroProcessor.processMacros(inputFile);
            MacroProcessor.writeToFile(expandedCode, outputFile);
            System.out.println("Expansão de macros concluída com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao processar macros: " + e.getMessage());
            return; // Se houver erro, encerramos a execução
        }
        
        // Inicializa Memória e Registradores
        memoria = new Memoria();
        registradores = new Registradores();

        registradores.registradores[0].setReg((byte) 0x01, (byte) 0x02, (byte) 0x03);
        memoria.memoria.get(0).setValor((byte) 0x01, (byte) 0x02, (byte) 0x03);
        memoria.memoria.get(1).setValor((byte) 0x0A, (byte) 0x0B, (byte) 0x0C);

        // Carrega a interface gráfica
        FXMLLoader loader = new FXMLLoader(getClass().getResource("style.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        controller.updateRegistradores(registradores);
        controller.updateMemoria(memoria);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simulador SIC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
