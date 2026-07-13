package com.examen;

import java.util.function.BooleanSupplier;

public class Partida {

    private String fecha;
    private int hasWon;
    private int isFirstTower;
    private int isFirstBlood;
    private int kills;
    private int deaths;
    private int assists;

    public Partida(String fecha, int b, int c, int d,
                   int kills, int deaths, int assists) {

        this.fecha = fecha;
        this.hasWon = b;
        this.isFirstTower = c;
        this.isFirstBlood = d;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;

    }

    public Partida(String fecha2, boolean b, boolean c, boolean d, int kills2, int deaths2, int assists2) {
		// TODO Auto-generated constructor stub
	}

	public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getHasWon() {
        return hasWon;
    }

    public void setHasWon(int hasWon) {
        this.hasWon = hasWon;
    }

    public int getIsFirstTower() {
        return isFirstTower;
    }

    public void setIsFirstTower(int isFirstTower) {
        this.isFirstTower = isFirstTower;
    }

    public int getIsFirstBlood() {
        return isFirstBlood;
    }

    public void setIsFirstBlood(int isFirstBlood) {
        this.isFirstBlood = isFirstBlood;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

	public BooleanSupplier isGano() {
		// TODO Auto-generated method stub
		return null;
	}

	public BooleanSupplier isPrimeraSangre() {
		// TODO Auto-generated method stub
		return null;
	}

	public BooleanSupplier isPrimerTorreta() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getAsesinatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getMuertes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getAsistencias() {
		// TODO Auto-generated method stub
		return null;
	}

}
