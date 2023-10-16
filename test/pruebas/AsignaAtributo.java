package pruebas;


public class AsignaAtributo {

    public static void main(String[] args) {
        pruebaEntity();
    }

    static void pruebaEntity() {
        EntidadA a = new EntidadA();
        a.setId(35);
        a.setNombre("algo");

        a.setPropertyStringValue("Bajo.tam", "16");
        //a.setPropertyStringValue("id2", "16");

        System.out.println(a);
    }
}

