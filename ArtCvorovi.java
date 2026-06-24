public class ArtCvorovi{	
	//promene se takode nalaze u graf.java!
	//ispiracija uzeta od Tarjanovog algoritma i sledece web stranice
	//https://www.geeksforgeeks.org/dsa/articulation-points-or-cut-vertices-in-a-graph/
	//algoritam radi na ideji,
	//ako je najmanja moguca dubina (min[w]) suseda cvora v >= vremenu pronalaska (dft[v]) , tada je cvor artikulacioni, 
	//tj cvor ce biti artikulacioni je jedini nacin prelaza susednih cvorova kroz njega
	// (ako ima drugi prolaz, min suseda ce biti manji, jer ima drugi put do ostatka grafa)
	 static void ispisiNiz(int[] w){
        System.out.print("[ ");
        for(int i =0; i < w.length; i++){
            System.out.print(w[i]);
        }
        System.out.println(" ]");
    }
    static void ispisiNizRaz0(int[] w){
        for(int i =0; i < w.length; i++){
            if(w[i] != 0){
				System.out.print(i + " ");
			}
        }
    }
    static void postaviNiz(int[] w){
        for(int i =0; i < w.length; i++){
            w[i] = 0;
        }
    }
	public static void main(String[] args){
		Graf g = new Graf(10);
		g.dodajGranu(0,1);
		g.dodajGranu(1,2);
		g.dodajGranu(2,3);
		g.dodajGranu(3,4);
		g.dodajGranu(0,4);
		g.dodajGranu(3,5);
		g.dodajGranu(5,6);
		g.dodajGranu(6,7);
		g.dodajGranu(7,8);
		g.dodajGranu(8,9);
		g.dodajGranu(9,7);
		TimeStamps dft = new TimeStamps(10);
		TimeStamps min = new TimeStamps(10); //predstavlja minimalnu dubinu koju mogu dostici susedi / sam cvor
		int[] artCvorovi = new int[10];
		postaviNiz(artCvorovi);
		for(int i = 0; i < 10; i++){ //for petlja dodata da postoji mogucnost ispitivanja nepovezanih grafova
			if(dft.theStampOf(i) == -1){
				//System.out.println("Pozvan DFS sa cvora broj " + i);	
				g.DFSArt(i,dft,min,artCvorovi,-1);
			}
		}
		
		System.out.println("Artikulacioni cvorovi su: ");
		ispisiNizRaz0(artCvorovi);
		/* test funkcionalnosti da detektuje da je pocetni cvor (0) artikulacioni
		Graf g = new Graf(7);
		g.dodajGranu(0,1);
		g.dodajGranu(1,2);
		g.dodajGranu(1,3);
		g.dodajGranu(1,4);
		g.dodajGranu(0,5);
		g.dodajGranu(5,6);
		TimeStamps dft = new TimeStamps(7);
		TimeStamps min = new TimeStamps(7); 
		int[] artCvorovi = new int[7];
		postaviNiz(artCvorovi);
		for(int i = 0; i < 5; i++){ //for petlja dodata da postoji mogucnost ispitivanja nepovezanih grafova
			if(dft.theStampOf(i) == -1){
				System.out.println("Pozvan DFS sa cvora broj " + i);	
				g.DFSArt(i,dft,min,artCvorovi,-1);
			}
		}
		System.out.println("Artikulacioni cvorovi su: ");
		ispisiNizRaz0(artCvorovi);*/
	}
}
