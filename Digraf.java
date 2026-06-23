import java.io.*;
import java.util.*;

public class Digraf {

  public final static int NoVertex = -1;

  private int N; // broj cvorova grafa
  private int M; // broj grana grafa
  private int[] outD; // izlazni stepen cvora
  private int[] inD; // ulazni stepen cvora
  private int[][] A; // matrica susedstva

  // konstruktori
  
  public Digraf(String fname) {
    try {
      SimpleIO.openFileInput(fname);
      N = SimpleIO.rdInt();
      inD = new int[N];
      outD = new int[N];
      for(int i = 0; i < N; i++) { inD[i] = 0; outD[i] = 0; }
      A = new int[N][N];
      M = 0;
      for(int i = 0; i < N; i++) {
        for(int j = 0; j < N; j++) {
          A[i][j] = SimpleIO.rdInt();
          if(A[i][j] != 0) { inD[j]++; outD[i]++; M++; }
        }
      }
    } catch (FileNotFoundException fn) {
      System.out.println("File not found: " + fn);
    } catch (Exception ex) {
      System.out.println(ex);
    }
    SimpleIO.closeInput();
  }

  public Digraf(int nn) {
    N = nn; M = 0;
    inD = new int[N];
    outD = new int[N];
    A = new int[N][N];
    for(int i = 0; i < N; i++) {
        inD[i] = 0; outD[i] = 0;
        for(int j = 0; j < N; j++){ A[i][j] = 0; }
    }
  }

  // osnovni parametri
  
  public int brCvorova() { return N; }

  public int brGrana() { return M; }

  public int outdeg(int v) { return outD[v]; }
  public int indeg(int v) { return inD[v]; }

  public boolean dominira(int u, int v) { return A[u][v] != 0; }

  public boolean susedni(int u, int v) { return dominira(u, v) || dominira(v, u); }

  public int tezina(int u, int v) { return A[u][v]; }
  
  // dodavanje i oduzimanje grane
  
  public void dodajGranu(int u, int v, int w){
    outD[u]++; inD[v]++; M++;
    A[u][v] = w;
  }

  public void dodajGranu(int u, int v){
    dodajGranu(u, v, 1);
  }

  public void ukloniGranu(int u, int v){
    outD[u]--; inD[v]--; M--;
    A[u][v] = 0;
  }

  // iterator po skupu suseda cvora

  private void nadjiSledeciCvor(SkupSuseda S) {
    while(true) {
      S.i++;
      if(S.i >= N) break;
      if(dominira(S.v, S.i)) break;
    }
  }

  // vraca skup izlaznih suseda

  public SkupSuseda sledbenici(int v) {
    SkupSuseda S = new SkupSuseda(v, NoVertex);
    nadjiSledeciCvor(S);
    return S;
  }
  
  public boolean imaJos(SkupSuseda S) { return S.i < N; }
  
  public int sledeci(SkupSuseda S) {
    int v = S.i;
    nadjiSledeciCvor(S);
    return v;
  }

  // osnovna implementacija DFS algoritma
  // povezanost, komponente, mostovi i artikulacioni cvorovi
  // provera da li je graf stablo
  
  public void DFS(int v, TimeStamps dft) {
    dft.stamp(v);
    SkupSuseda S = sledbenici(v);
    while(imaJos(S)){
      int w = sledeci(S);
      if(!dft.stamped(w)) DFS(w, dft);
    }
  }

  public void DFS(int v, TimeStamps dft, int stamp) {
    dft.stamp(v, stamp);
    SkupSuseda S = sledbenici(v);
    while(imaJos(S)){
      int w = sledeci(S);
      if(!dft.stamped(w)) DFS(w, dft, stamp);
    }
  }

  // slaba povezanost

  public Digraf simetrizuj() {
    Digraf d = new Digraf(N);
    for(int i = 0; i < N; i++) {
        for(int j = 0; j < N; j++) {
            if(i != j && dominira(i, j)) {
                d.dodajGranu(i, j); d.dodajGranu(j, i);
            }
        }
    }
    return d;
  }

  // topolosko sortiranje aciklicnog grafa

  public void TopSortDFS(int v, TimeStamps dft, Stack<Integer> T) {
    dft.stamp(v);
    SkupSuseda S = sledbenici(v);
    while(imaJos(S)){
      int w = sledeci(S);
      if(!dft.stamped(w)) TopSortDFS(w, dft, T);
    }
    T.push(v);
  }


  public Stack<Integer> TopSort() {
    Stack<Integer> S = new Stack<Integer>();
    TimeStamps dft = new TimeStamps(N);
    for(int v = 0; v < N; v++) {
        if(!dft.stamped(v)) { TopSortDFS(v, dft, S); }
    }
    return S;
  }

 
  public int[][] Floyd() {
    int[][] D = new int[N][N];
    int i, j, k;
    for(i = 0; i < N; i++)
      for(j = 0; j < N; j++)
        D[i][j] = tezina(i, j);

    for(k = 0; k < N; k++)
      for(i = 0; i < N; i++)
        for(j = 0; j < N; j++)
            if(
              i != j && i != k && j != k && D[i][k] != 0 && D[k][j] != 0
              &&(D[i][j] == 0 || D[i][k] + D[k][j] < D[i][j])
            ) {
              D[i][j] = D[i][k] + D[k][j];
            }
    return D;
  }

  public int[][][] FloydP() {
    int[][][] podaci = new int[2][N][N];
    int[][] D = podaci[0];
    int[][] P = podaci[1];
    int i, j, k;
    for(i = 0; i < N; i++)
      for(j = 0; j < N; j++) {
        D[i][j] = tezina(i, j);
        P[i][j] = NoVertex;
      }

    for(k = 0; k < N; k++)
      for(i = 0; i < N; i++)
        for(j = 0; j < N; j++)
            if(
              i != j && i != k && j != k && D[i][k] != 0 && D[k][j] != 0
              &&(D[i][j] == 0 || D[i][k] + D[k][j] < D[i][j])
            ) {
              D[i][j] = D[i][k] + D[k][j];
              P[i][j] = k;
            }
    return podaci;
  }

  
  private int minPozNeozn(int[] D, TimeStamps dft) {
    int x = NoVertex;
    for(int i = 0; i < N; i++)
      if(D[i] > 0 && !dft.stamped(i)) {
        x = i; break;
      }
    if(x == NoVertex) return NoVertex;

    for(int i = x + 1; i < N; i++)
      if(D[i] > 0 && !dft.stamped(i) && D[i] < D[x]) x = i;
    
    return x;
  }
  
  public int[] Dijkstra(int src) {
    int[] D = new int[N];
    for(int i = 0; i < N; i++) D[i] = 0;
    TimeStamps dft = new TimeStamps(N);
    
    dft.stamp(src);
    SkupSuseda S = sledbenici(src);
    while(imaJos(S)) {
      int x = sledeci(S);
      D[x] = tezina(src, x);
    }
    
    while(true) {
      int v = minPozNeozn(D, dft);
      if(v == NoVertex) break;
      dft.stamp(v);
      S = sledbenici(v);
      while(imaJos(S)) {
        int x = sledeci(S);
        if(!dft.stamped(x)) {
          if(D[x] == 0) D[x] = D[v] + tezina(v, x);
          else D[x] = Math.min(D[x], D[v] + tezina(v, x));
        }
      }
    }
    
    return D;
  }

  public int[][] DijkstraP(int src) {
    int[][] podaci = new int[2][N];  
    int[] D = podaci[0];
    int[] P = podaci[1];
    for(int i = 0; i < N; i++) D[i] = 0;
    
    TimeStamps dft = new TimeStamps(N);
    dft.stamp(src);
    P[src] = NoVertex;

    SkupSuseda S = sledbenici(src);
    while(imaJos(S)) {
      int x = sledeci(S);
      D[x] = tezina(src, x);
      P[x] = src;
    }
    
    while(true) {
      int v = minPozNeozn(D, dft);
      if(v == NoVertex) break;
      dft.stamp(v);
      S = sledbenici(v);
      while(imaJos(S)) {
        int x = sledeci(S);
        if(!dft.stamped(x)) {
          if(D[x] == 0) {
            D[x] = D[v] + tezina(v, x);
            P[x] = v;
          }
          else if(D[v] + tezina(v, x) < D[x]) {
            D[x] = D[v] + tezina(v, x);
            P[x] = v;
          }
        }
      }
    }
    
    return podaci;
  }

}

















