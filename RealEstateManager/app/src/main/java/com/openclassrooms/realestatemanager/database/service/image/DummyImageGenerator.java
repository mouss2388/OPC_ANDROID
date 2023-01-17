package com.openclassrooms.realestatemanager.database.service.image;

import com.openclassrooms.realestatemanager.database.model.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyImageGenerator {

    public static List<Image> DUMMY_IMAGES =
            Arrays.asList(

                    new Image(1L, "https://media.istockphoto.com/id/1273552068/fr/photo/vue-ext%C3%A9rieure-de-limmeuble-dappartements-moderne.jpg?s=612x612&w=0&k=20&c=jJuGUqsiYjd5gLwoNrPmbGmWBFFrOLmdQLFAHp9qtt8="),
                    new Image(1L, "https://cdn.pixabay.com/photo/2014/07/31/21/41/apartment-406901_960_720.jpg"), new Image(1L, "https://media.istockphoto.com/id/1199873461/fr/photo/appartement-de-conception-moderne-vide-de-marque-pour-la-location.jpg?s=612x612&w=0&k=20&c=mD7hDR1qavmGznd2-8InhSg_RqKVUL_mtafsHktSrnY="),

                    new Image(2L, "https://media.istockphoto.com/id/1177797403/fr/photo/immeubles-dappartements-modernes-sur-une-journ%C3%A9e-ensoleill%C3%A9e-avec-un-ciel-bleu.jpg?s=612x612&w=0&k=20&c=RYVqzUo-p9hfMYyHIQyicLMgO84SjLZe70j_CNXaAI0="),
                    new Image(2L, "https://media.istockphoto.com/id/1227178152/fr/photo/int%C3%A9rieur-moderne-de-salon.jpg?s=612x612&w=0&k=20&c=MBpQ_-jkcmeS2SwqYixLb8XfkTb7BH7ioyePd_s74Zs="),

                    new Image(3L, "https://cdn.pixabay.com/photo/2016/09/26/12/02/halic-castle-1695806__340.jpg"),
                    new Image(3L, "https://media.istockphoto.com/id/536291001/fr/photo/chambre-familiale-spacieuse.jpg?s=612x612&w=0&k=20&c=HXe88NATlWcHrzSIvETHJSwt-PE6ClTPr857pFW7ygA="),

                    new Image(4L, "https://media.istockphoto.com/id/1201803928/fr/photo/maison-moderne-avec-terrasse-et-patio.jpg?s=612x612&w=0&k=20&c=oa9FL0b5JqbGn2p-SE_auVp_o34Ycqy8ideHcUVbYEM="),
                    new Image(4L, "https://cdn.pixabay.com/photo/2017/03/28/12/10/chairs-2181947__340.jpg"),
                    new Image(4L, "https://cdn.pixabay.com/photo/2017/03/28/12/13/chairs-2181968__340.jpg"),
                    new Image(4L, "https://cdn.pixabay.com/photo/2020/11/24/11/36/bedroom-5772286__340.jpg"),

                    new Image(5L, "https://media.istockphoto.com/id/175767618/fr/photo/maison-moderne-complexe.jpg?s=612x612&w=0&k=20&c=7KnnS0s_xxZnPtbAyMYpm9KkJiPQPe_pILrRHl3Lexs="),
                    new Image(5L, "https://media.istockphoto.com/id/1251269544/fr/photo/chambre-domestique-de-cuisine-de-maison-de-ville-dappartement-et-salon.jpg?s=612x612&w=0&k=20&c=D0lr-ZW-HRobxsOzFRpmjNbpx3vL7G3SljyNtDCbyPo=")

            );

    static List<Image> generateImages() {
        return new ArrayList<>(DUMMY_IMAGES);
    }

}
