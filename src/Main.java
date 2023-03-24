import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static String URL = "jdbc:mysql://localhost:3306/db-nations";
    private static String USER = "root" ;
    private static String PASSWORD = "root";
    private static String FORMAT_TABLE = "%10s%30s%30s%30s\n";


    public static void main(String[] args) {

            try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
                //System.out.println(conn.isClosed());

                // nome country, id, nome della regione e nome del continente,
                String query = """
                        SELECT  c.name AS nation_name, c.country_id AS nation_id, r.name AS region_name, c2.name AS continent_name
                        from countries c\s
                        join regions r on c.region_id = r.region_id\s
                        join continents c2 on c2.continent_id = r.continent_id
                        where c.name like ?
                        order by c.name ;
                        """;
                //tutte le lingua del country
                String langQuery = """
                        select l.language
                        from country_languages cl
                        join languages l on cl.language_id = l.language_id
                        where cl.country_id = ?
                        order by l.`language`;
                        """;
                //tutte le statuistiche più recenti
                String statsQuery = """
                                select  cs.population, cs.gdp
                                from country_stats cs
                                where cs.country_id = ?
                                order by `year` desc
                                limit 1;
                               """;



                //la connection prepara uno statement sql dalla query in parametro
                try(PreparedStatement ps = conn.prepareStatement(query)){

                    boolean hasResults = false; // tiene traccia della query nations

                    //chiedo all'utente una parola per la ricerca
                    Scanner scan = new Scanner(System.in);
                    System.out.print("Insert a filter key: ");
                    String keyWord = scan.nextLine();


                    //associo il parametro per evitare SQL Injections
                    ps.setString(1, "%" + keyWord + "%");

                    //eseguo la query e il risultato va nel ResultSet
                    try(ResultSet rs = ps.executeQuery()){
                        if(rs.next()){
                            hasResults = true;
                            do{
                                //definisco il contenuto del ResultSet
                            String nation_name = rs.getString("nation_name");
                            Integer nation_id = rs.getInt("nation_id");
                            String region_name = rs.getString("nation_name");
                            String continent_name = rs.getString("continent_name");

                            System.out.printf(FORMAT_TABLE,nation_name,nation_id,region_name,continent_name);

                        }while(rs.next());
                    }else{
                            System.out.println("No countries found");
                        }

                    if(hasResults){
                        //BONUS lingue e statistiche
                        System.out.println("Insert an id of this country : ");
                        int id_country = Integer.parseInt(scan.nextLine());

                        try (PreparedStatement psLang = conn.prepareStatement(langQuery)){
                            psLang.setInt(1, id_country);

                            try(ResultSet rsLang = psLang.executeQuery()){
                                System.out.println("Languages: ");
                                while(rsLang.next()){
                                    System.out.print(rsLang.getString(1));
                                if(!rsLang.isLast()){
                                    System.out.print(", ");
                                }else{
                                    System.out.println();
                                }
                            }
                        }
                    }


                        try (PreparedStatement psStats = conn.prepareStatement(statsQuery)){
                            psStats.setInt(1, id_country);
                            try (ResultSet rsStats = psStats.executeQuery()){
                                if(rsStats.next()){
                                    int year = rsStats.getInt(1);
                                    BigDecimal population = rsStats.getBigDecimal(2);
                                    BigDecimal gdb = rsStats.getBigDecimal(3);
                                    System.out.println("Stats for year " + year + ": ");
                                }

                            }
                        }


               }



            }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
}