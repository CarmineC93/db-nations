import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
            String URL = "jdbc:mysql://localhost:3306/db-nations";
            String USER = "root" ;
            String PASSWORD = "root";

            //chiedo all'utente una parola per la ricerca
            Scanner scan = new Scanner(System.in);
            System.out.print("Insert a filter key: ");
            String keyWord = scan.nextLine();

            try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
                //System.out.println(conn.isClosed());

                //          nome, id, nome della regione e nome del continente,
                String query = """
                        SELECT  c.name AS nation_name, c.country_id AS nation_id, r.name AS region_name, c2.name AS continent_name
                        from countries c\s
                        join regions r on c.region_id = r.region_id\s
                        join continents c2 on c2.continent_id = r.continent_id
                        where c.name like ?
                        order by c.name ;
                        """;

                //la connection prepara uno statement sql dalla query in parametro
                try(PreparedStatement ps = conn.prepareStatement(query)){

                    //associo il parametro per evitare SQL Injections
                    ps.setString(1, "%" + keyWord + "%");

                    //eseguo la query e il risultato va nel ResultSet
                    try(ResultSet rs = ps.executeQuery()){
                        //definisco il contenuto del ResultSet
                        while(rs.next()){
                            String nation_name = rs.getString("nation_name");
                            Integer id = rs.getInt("nation_id");
                            String region_name = rs.getString("nation_name");
                            String continent_name = rs.getString("continent_name");

                            System.out.println(
                                    nation_name + " " +  id + " " + region_name + " " + continent_name);

                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
}