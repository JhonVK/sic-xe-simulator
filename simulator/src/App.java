import Mem.Memoria;
import Regs.Registradores;
import Ligador.Ligador;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Memoria memoria;
    private static Registradores registradores;
    private static Ligador ligador;
    private static Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializa Memória e Registradores
        memoria = new Memoria();
        registradores = new Registradores();
        ligador = new Ligador();

        // Carrega a interface gráfica
        FXMLLoader loaderFXML = new FXMLLoader(getClass().getResource("style.fxml"));
        Parent root = loaderFXML.load();

        controller = loaderFXML.getController();
        controller.setStage(primaryStage);
        controller.updateRegistradores(registradores);
        controller.updateMemoria(memoria);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styless.css").toExternalForm());
        primaryStage.setTitle("Simulador SIC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void LII() {
        try {
            System.out.println("--- Memória antes da execução ---");
            ligador.printMemory(memoria, 0, 10);
            
            // Executa o ligador
            ligador.pass1();
            ligador.pass2(memoria, registradores);
            
            System.out.println("--- Memória depois da execução ---");
            ligador.printMemory(memoria, 0, 200);
            
            // Atualiza a interface gráfica com os novos valores
            if (controller != null) {
                controller.updateRegistradores(registradores);
                controller.updateMemoria(memoria);
            } else {
                System.err.println("Erro: Controller não inicializado!");
            }
        } catch (Exception e) {
            System.err.println("Erro durante a execução do ligador: " + e.getMessage());
            e.printStackTrace();
            // Você poderia adicionar código aqui para mostrar o erro na interface gráfica
        }
    }
}