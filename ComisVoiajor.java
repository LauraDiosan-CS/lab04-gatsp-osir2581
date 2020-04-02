package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class ComisVoiajor {
    private String fisier;
    private int nrOrase;
    private ArrayList<ArrayList<Integer>> matrice;
    private ArrayList<ArrayList<Double>> matriceFeromoni;
    private ArrayList<ArrayList<Double>> copieMatriceFeromoni;
    private ArrayList<ArrayList<Integer>> copieMatrice;
    public ComisVoiajor(String file){
        fisier=file;
        loadFromFile();
    }
    public void loadFromFile(){
        File f = new File(fisier);
        try {
            Scanner sc = new Scanner(f);
            nrOrase=Integer.parseInt(sc.nextLine());
            matrice = new ArrayList<>(nrOrase);
            matriceFeromoni = new ArrayList<>(nrOrase);
            copieMatriceFeromoni = new ArrayList<>(nrOrase);
            copieMatrice = new ArrayList<>(nrOrase);
            for(int i=0;i<nrOrase;i++) {
                String linie = sc.nextLine();
                String[] params = linie.split(",");
                ArrayList<Integer> line = new ArrayList<>();
                ArrayList<Double> linie2 = new ArrayList<>();
                ArrayList<Double> linie3 = new ArrayList<>();
                ArrayList<Integer> linie4 = new ArrayList<>();
                for(String p : params){
                    line.add(Integer.parseInt(p));
                    linie2.add((double)0);
                    linie3.add((double)0);
                    linie4.add(0);
                }
                matrice.add(line);
                matriceFeromoni.add(linie2);
                copieMatriceFeromoni.add(linie3);
                copieMatrice.add(linie4);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu exista!");
        }
    }

    public ArrayList<Integer> ceaMaiBunaSolutie(ArrayList<ArrayList<Integer>> furnicute){
        int cost = cost(furnicute.get(0));
        ArrayList<Integer> ceaMaiBuna = new ArrayList<>(nrOrase);
        for(int i=0;i<nrOrase;i++)
            ceaMaiBuna.add(furnicute.get(0).get(i));
        for(int i=0;i<furnicute.size();i++) {
            int costAux = cost(furnicute.get(i));
            if (cost > costAux) {
                cost = costAux;
                for(int j=0;j<nrOrase;j++){
                    ceaMaiBuna.set(j,furnicute.get(i).get(j));
                }
            }
        }
        return ceaMaiBuna;
    }

    public int cost(List<Integer> permutare){
        int c=0,i;
        for(i=0;i<permutare.size()-1;i++){
            c+=matrice.get(permutare.get(i)).get(permutare.get(i+1));
        }
        c+=matrice.get(permutare.get(i)).get(permutare.get(0));
        return c;
    }

    public int nextOras(ArrayList<Integer> oraseParcurse, int start){
        double raport = -1;
        int oras=0;
        Random r = new Random();
        float p = r.nextFloat();
        int orasSecundar = r.nextInt(nrOrase);
            for( int j =0 ;j< matriceFeromoni.size();j++){
                int k=0;
                for (Integer integer : oraseParcurse) {
                    if (integer == j) {
                        k = 1;
                        break;
                    }
                }
                if(k==0)
                    if (matrice.get(start).get(j)!=0) {
                        double raportNou = (double) matriceFeromoni.get(start).get(j) / matrice.get(start).get(j);
                        if (raportNou > raport) {
                            raport = raportNou;
                            oras = j;
                        }
                    }
            }
        if(p>0.9) {
            int k=0;
            for (Integer integer : oraseParcurse) {
                if (integer == orasSecundar) {
                    k = 1;
                    break;
                }
            }
            if (orasSecundar == start || k==1) {
                return oras;
            }
            else
                return orasSecundar;
        }
        else return oras;
    }

    public void updateazaFeromon(int sursa, int destinatie){
        //copieMatriceFeromoni.get(sursa).set(destinatie,0.9*matriceFeromoni.get(sursa).get(destinatie)+1);
        matriceFeromoni.get(sursa).set(destinatie,0.9*matriceFeromoni.get(sursa).get(destinatie)+1);
    }

    public void solutii(int nrIteratii) {
        Random r = new Random();
        int index = 0;
        int muchie1;
        int muchie2;
        muchie1 = r.nextInt(nrOrase - 1);
        muchie2 = r.nextInt(nrOrase - 1);
        int nrFurnicute = r.nextInt(nrOrase)+1;
        ArrayList<Integer> ceaMaiBunaSolutie;
        while(index<nrIteratii) {
            ArrayList<ArrayList<Integer>> furnicute = new ArrayList<>(nrFurnicute);
            int d = 0;
            for (int i = 0; i < nrFurnicute; i++) {
                ArrayList<Integer> solutieFiecareFurnicuta = new ArrayList<>(nrOrase);
                for (int j = 0; j < nrOrase; j++)
                    solutieFiecareFurnicuta.add(-1);
                solutieFiecareFurnicuta.set(0, r.nextInt(nrOrase));
                furnicute.add(solutieFiecareFurnicuta);
            }
            for (int i = 0; i < nrFurnicute; i++) {
                for (int j = 0; j < nrOrase - 1; j++) {
                    int oras = nextOras(furnicute.get(i), furnicute.get(i).get(d));
                    updateazaFeromon(furnicute.get(i).get(d), oras);
                    d++;
                    furnicute.get(i).set(d,oras);
                }
                d = 0;
            }

            ceaMaiBunaSolutie = ceaMaiBunaSolutie(furnicute);
            int cost = cost(ceaMaiBunaSolutie);
            for (int i = 0; i < nrOrase - 1; i++) {
                updateazaFeromon(ceaMaiBunaSolutie.get(0), ceaMaiBunaSolutie.get(i));
            }
            //matriceFeromoni=copieMatriceFeromoni;
            System.out.println(cost + " " + ceaMaiBunaSolutie);
            index++;
           if(index%5==0) {
                muchie1 = r.nextInt(nrOrase - 1);
                muchie2 = r.nextInt(nrOrase - 1);
                if(muchie1 == muchie2)
                    muchie2 = muchie2+1;
                copieMatrice.get(muchie1).set(muchie2,matrice.get(muchie1).get(muchie2));
                copieMatrice.get(muchie2).set(muchie1,matrice.get(muchie1).get(muchie2));
                matrice.get(muchie1).set(muchie2,0);
                matrice.get(muchie2).set(muchie1,0);
                matriceFeromoni.get(muchie1).set(muchie2,(double)0);
                matriceFeromoni.get(muchie2).set(muchie1,(double)0);
            }
            if(index%6==0){
                matrice.get(muchie1).set(muchie2,copieMatrice.get(muchie1).get(muchie2));
                matrice.get(muchie2).set(muchie1,copieMatrice.get(muchie1).get(muchie2));
            }
        }
    }



    public void afiseaza(){
        for(int i=0;i<nrOrase;i++) {
            for (int j = 0; j < nrOrase; j++)
                System.out.print(matrice.get(i).get(j) + " ");
            System.out.println();
        }
    }


}
