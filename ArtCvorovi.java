public class ArtCvorovi {
    public static void main(String[] args){
        Graf g = new Graf(10);
        g.dodajGranu(0,1);
        g.dodajGranu(0,4);
        g.dodajGranu( 1,2);
        g.dodajGranu(2,3);
        g.dodajGranu(4,3);
        g.dodajGranu(3,5);
        g.dodajGranu(5,6);
        g.dodajGranu(6,7);
        g.dodajGranu(7,9);
        g.dodajGranu(9,8);
        g.dodajGranu(7,8); //uradjeno na ovaj nacin iz razloga sto nisam skontao metod preko txt filea

        
    }
}
