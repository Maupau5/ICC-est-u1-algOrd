import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class App {

    // Tamaños de los arreglos
    private static final int[] SIZES = { 10, 100, 1000, 5000, 10000, 30000 };
    private static final int MAX_VALUE = 30000;
    private static int[][] arrays = new int[SIZES.length][]; // Arreglos generados

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Generar Arreglos Aleatorios");
            System.out.println("2. Ordenar por los 3 métodos");
            System.out.println("3. Buscar valores (Búsqueda Binaria Normal y Recursiva)");
            System.out.println("4. Salir");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    generateRandomArrays();
                    System.out.println("Arreglos generados con éxito.");
                    break;
                case 2:
                    sortAndMeasureTimes();
                    break;
                case 3:
                    searchAndMeasureTimes(scanner);
                    break;
                case 4:
                    exit = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    // Genera arreglos aleatorios
    private static void generateRandomArrays() {
        Random rand = new Random();
        int[] baseArray = new int[SIZES[SIZES.length - 1]];
        for (int i = 0; i < baseArray.length; i++) {
            baseArray[i] = rand.nextInt(MAX_VALUE) + 1;
        }
        for (int i = 0; i < SIZES.length; i++) {
            arrays[i] = Arrays.copyOfRange(baseArray, 0, SIZES[i]);
        }
    }

    // Realiza el ordenamiento de cada arreglo con los 3 métodos
    private static void sortAndMeasureTimes() {
        for (String methodName : new String[] { "Burbuja con Ajuste", "Selección", "Inserción" }) {
            System.out.println("Método " + methodName);
            for (int i = 0; i < SIZES.length; i++) {
                int[] testArray = Arrays.copyOf(arrays[i], arrays[i].length);

                Runnable sortingMethod;
                switch (methodName) {
                    case "Burbuja con Ajuste":
                        sortingMethod = () -> bubbleSortWithAdjustment(testArray);
                        break;
                    case "Selección":
                        sortingMethod = () -> selectionSort(testArray);
                        break;
                    case "Inserción":
                        sortingMethod = () -> insertionSort(testArray);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + methodName);
                }

                double time = measureTime(sortingMethod);
                System.out.printf("Con %d valores el tiempo es de %.4f seg.%n", SIZES[i], time);
            }
            System.out.println();
        }
    }

    // Realiza la búsqueda de un valor específico
    private static void searchAndMeasureTimes(Scanner scanner) {
        System.out.print("Ingrese el valor a buscar: ");
        int value = scanner.nextInt();

        for (int i = 0; i < SIZES.length; i++) {
            int[] sortedArray = Arrays.copyOf(arrays[i], arrays[i].length);
            Arrays.sort(sortedArray);

            double normalTime = measureTime(() -> binarySearch(sortedArray, value));
            double recursiveTime = measureTime(
                    () -> binarySearchRecursive(sortedArray, value, 0, sortedArray.length - 1));

            System.out.printf("En arreglo de tamaño %d:%n", SIZES[i]);
            System.out.printf("  Búsqueda binaria normal tiempo: %.4f seg.%n", normalTime);
            System.out.printf("  Búsqueda binaria recursiva tiempo: %.4f seg.%n", recursiveTime);
        }
    }

    // Métodos de ordenamiento
    private static void bubbleSortWithAdjustment(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }           
            }
        }
    }

    private static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    private static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Búsqueda binaria iterativa
    private static int binarySearch(int[] arr, int value) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == value)
                return mid;
            if (arr[mid] < value)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }

    // Búsqueda binaria recursiva
    private static int binarySearchRecursive(int[] arr, int value, int left, int right) {
        if (left > right)
            return -1;
        int mid = left + (right - left) / 2;
        if (arr[mid] == value)
            return mid;
        if (arr[mid] < value)
            return binarySearchRecursive(arr, value, mid + 1, right);
        return binarySearchRecursive(arr, value, left, mid - 1);
    }

    // Medir el tiempo de ejecución de un método
    private static double measureTime(Runnable method) {
        long start = System.nanoTime();
        method.run();
        long end = System.nanoTime();
        return (end - start) / 1e9; // De nanosegundos a segundos
    }
}