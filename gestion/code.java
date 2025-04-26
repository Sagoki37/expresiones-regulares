import java.util.Scanner;
import java.util.regex.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ValidadorConcurrente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(3); // puedes ajustar el número de hilos

        System.out.print("¿Cuántas contraseñas desea validar? ");
        int n = sc.nextInt();
        sc.nextLine(); // limpiar buffer

        for (int i = 1; i <= n; i++) {
            System.out.print("Ingrese la contraseña #" + i + ": ");
            String password = sc.nextLine();
            executor.execute(new ValidadorPassword(password, i));
        }

        executor.shutdown();
    }
}

// 🔒 Clase que valida una contraseña en un hilo separado
class ValidadorPassword implements Runnable {
    private final String password;
    private final int id;

    public ValidadorPassword(String password, int id) {
        this.password = password;
        this.id = id;
    }

    @Override
    public void run() {
        boolean esValida = validar(password);

        System.out.println("🔍 Contraseña #" + id + ": " +
            (esValida ? "✅ Válida" : "❌ Inválida"));
    }

    private boolean validar(String pwd) {
        if (pwd.length() < 8) return false;

        // Expresiones regulares individuales para los requisitos
        Pattern mayusculas = Pattern.compile("[A-Z]");
        Pattern minusculas = Pattern.compile("[a-z]");
        Pattern numeros = Pattern.compile("\\d");
        Pattern especiales = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

        // Contar ocurrencias usando Matcher
        int countMayus = contarCoincidencias(mayusculas, pwd);
        int countMinus = contarCoincidencias(minusculas, pwd);
        int countNum = contarCoincidencias(numeros, pwd);
        int countEsp = contarCoincidencias(especiales, pwd);

        return countMayus >= 2 &&
               countMinus >= 3 &&
               countNum >= 1 &&
               countEsp >= 1;
    }

    private int contarCoincidencias(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
