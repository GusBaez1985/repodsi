package ar.edu.utn.frba.dds.domain.criterios;

public class CriterioFundamentacionMinima implements CriterioAprobacionSolicitud {
    public static Integer cantCaracteresMinimosAprobacion = 500;
    public Boolean cumpleCriterio(String motivo){
        if (motivo.length()>=cantCaracteresMinimosAprobacion) {
            return true;
        }else
            System.out.println("Motivo Demasiado Corto");
        return false;
    }
}