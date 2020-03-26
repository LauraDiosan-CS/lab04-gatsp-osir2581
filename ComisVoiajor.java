package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class ComisVoiajor {
    private String fisier;
    private int nrOrase;
    private ArrayList<ArrayList<Integer>> matrice;
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
            for(int i=0;i<nrOrase;i++) {
                String linie = sc.nextLine();
                String[] params = linie.split(",");
                ArrayList<Integer> line = new ArrayList<>();
                for(String p : params){
                    line.add(Integer.parseInt(p));
                }
                matrice.add(line);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fisierul nu exista!");
        }
    }

    public ArrayList<Integer> ceaMaiBunaSolutie(ArrayList<ArrayList<Integer>> generatie){
        int cost = cost(generatie.get(0));
        ArrayList<Integer> ceaMaiBuna = new ArrayList<>(nrOrase);
        for(int i=0;i<nrOrase;i++)
            ceaMaiBuna.add(generatie.get(0).get(i));
        for(int i=0;i<generatie.size();i++) {
            int costAux = cost(generatie.get(i));
            if (cost > costAux) {
                cost = costAux;
                for(int j=0;j<nrOrase;j++){
                    ceaMaiBuna.set(j,generatie.get(i).get(j));
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

    public ArrayList<Integer> crossOver(ArrayList<Integer> tata, ArrayList<Integer> mama){
        ArrayList<Integer> copil = new ArrayList<>(tata.size());
        Random r = new Random();
        int pozitie1 = r.nextInt(tata.size()-1);
        int pozitie2 = r.nextInt(tata.size());
        int aux;
        if(pozitie1 > pozitie2){
            aux = pozitie1;
            pozitie1 = pozitie2;
            pozitie2 = aux;
        }
        for(int i=pozitie1 ; i<=pozitie2;i++)
            copil.add(tata.get(i));
        int indexCurent = 0;
        int orasCurentMama = 0;
        for(int i =0 ;i < tata.size();i++){
            indexCurent = (pozitie2+i)%tata.size();
            orasCurentMama = mama.get(indexCurent);
            if(!copil.contains(orasCurentMama))
                copil.add(orasCurentMama);
        }
        Collections.rotate(copil,pozitie1);
        return copil;
    }

    public ArrayList<Integer> muteazaCopil(ArrayList<Integer> copil){
        Random r = new Random();
        int pozitie1 = r.nextInt(copil.size());
        int pozitie2 = r.nextInt(copil.size());
        int aux = copil.get(pozitie1);
        copil.set(pozitie1, copil.get(pozitie2));
        copil.set(pozitie2, aux);
        return copil;
    }

    public ArrayList<Integer> selecteaza(ArrayList<ArrayList<Integer>> generatie){
        Random r = new Random();
        int numarParticipantiTurneu = r.nextInt(generatie.size());
        ArrayList<Integer> alfa = generatie.get(r.nextInt(generatie.size()));
        int costAlfa = cost(alfa);
        for(int i=1;i<numarParticipantiTurneu;i++){
            ArrayList<Integer> beta = generatie.get(r.nextInt(generatie.size()));
            int costBeta = cost(beta);
            if(costAlfa > costBeta){
                alfa = beta;
                costAlfa = costBeta;
            }
        }
        return alfa;
    }

    public void solutii(int nrIteratii, int marimePopulatie) {
        int index = 0;
        ArrayList<ArrayList<Integer>> generatie = new ArrayList<>(50);
        ArrayList<Integer> ceaMaiBunaSolutie = new ArrayList<>(nrOrase);
        ArrayList<Integer> permutare = new ArrayList<>(nrOrase);
        for (int i = 0; i < nrOrase; i++)
            permutare.add(i);
        for (int i = 0; i < marimePopulatie; i++) {
            ArrayList<Integer> specimen = new ArrayList<>(nrOrase);
            Collections.shuffle(permutare);
            for (int j = 0; j < nrOrase; j++)
                specimen.add(permutare.get(j));
            generatie.add(specimen);
            ceaMaiBunaSolutie = ceaMaiBunaSolutie(generatie);
        }
        while (index < nrIteratii) {
            ArrayList<ArrayList<Integer>> generatieNoua = new ArrayList<>();
            generatieNoua.add(ceaMaiBunaSolutie);
            while(generatieNoua.size()<marimePopulatie) {
                ArrayList<Integer> tata;
                ArrayList<Integer> mama;
                ArrayList<Integer> copil;
                tata = selecteaza(generatie);
                mama = selecteaza(generatie);
                copil= (muteazaCopil(crossOver(tata,mama)));
                generatieNoua.add(copil);
            }
            generatie=generatieNoua;
            index++;
            ceaMaiBunaSolutie = ceaMaiBunaSolutie(generatie);
            System.out.println(ceaMaiBunaSolutie + " " + cost(ceaMaiBunaSolutie));
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
