package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	Graph<Album, DefaultWeightedEdge> grafo = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	ItunesDAO dao = new ItunesDAO();
	public Set<Album> successors = new HashSet<>();
	public List<bilancio> bilanci= new ArrayList<>();
	
	
	public void BuildGraph(int n) {
		
		//Mi costruisco i vertici
		
		for(Album a : dao.getVert(n)) {
			grafo.addVertex(a);
		}
		
		
		//Mi costruisco gli edges
		int peso;
		for(Album a : dao.getVert(n)) {
			for(Album a1 : dao.getVert(n)) {
				
				if(!a1.equals(a) && a1.getAlbumId()>a.getAlbumId()) {
					
					int subPeso1 = dao.getPesoEdge(a1.getAlbumId());
					int subPeso2 = dao.getPesoEdge(a.getAlbumId());
					peso = Math.abs(subPeso1 - subPeso2);
					if(peso>0) {
						if(subPeso1>subPeso2)
							Graphs.addEdgeWithVertices(grafo,a, a1, peso);
							
						else 
						
							Graphs.addEdgeWithVertices(grafo,a1, a, peso);

						
					}
					
				}
					
			}
		}
		
		
	}
	
	public int nVert() {
		return grafo.vertexSet().size();
	}
	
	public int nArch() {
		return grafo.edgeSet().size();
	}
	
	public void succ(Album a) {
		List<bilancio> bilanci = new ArrayList<>();
		
		//Mi sono preso i successori del nodo a
			Set<DefaultWeightedEdge> outgoing = grafo.outgoingEdgesOf(a);
			
			for(DefaultWeightedEdge edge : outgoing) {
				Album successivo = grafo.getEdgeTarget(edge);
				successors.add(successivo);
			}
	}
	
	//Mi calcolo il bilancio per i successori
	
	public void balance() {
		
		
		for(Album a3 : this.successors) {
			int in=balanceIN(a3);
			int out=balanceOUT(a3);
			this.bilanci.add(new bilancio(a3,in+out));
		}
		
		
		//Balance IN
		
		
		
		
		//Balance OUT
		
		
		//Stampa
		
		Collections.sort(this.bilanci);
		
	}
	
	public int balanceIN(Album a) {
		 int weightSum = 0;

	        Set<DefaultWeightedEdge> incomingEdges = grafo.incomingEdgesOf(a);

	        for (DefaultWeightedEdge edge : incomingEdges) {
	            weightSum += grafo.getEdgeWeight(edge);
	        }
	        
	        return weightSum;
}
	
	public int balanceOUT(Album a) {
	int weightSum = 0;

    Set<DefaultWeightedEdge> outgoingEdges = grafo.outgoingEdgesOf(a);

    for (DefaultWeightedEdge edge : outgoingEdges) {
        weightSum += grafo.getEdgeWeight(edge);
    }	
    return -weightSum;
    
}
	
}
	