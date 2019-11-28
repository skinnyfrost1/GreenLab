



class Card{
    constructor(){
        this.imageData = null;
        this.imageTitle = null;
        this.imageContent = null;
        this.imageId = null;
    }
    __ceateFakeData(){
        this.imageData = "https://ml4141715.github.io/water/cdn/image/Holder.png";
        this.imageTitle = "666imageTitle";
        this.imageContent = "7789imageContent";
        this.imageId = "675imageId";
    }
    // we will add some button here
    // so what we will add
    // favorite
    // duplicate
    // delete
    // <div class="card mb-1 grid-square">
    //         <img class="card-img-top img-fluid grid-photo" src="https://ml4141715.github.io/water/cdn/image/Holder.png" alt="" draggable="false">
    //         <div class="card-body">
    //         <p class="card-title h4 small font-weight-bold">Holder</p>
    //         <p class="card-text h5 small">Author: Homelander </p>
    // </div>
    // </div>
    __cardHTML(){
        var ReturnData = "<div class=\"card mb-1 grid-square\"  id=\""+this.imageId+"\" >\n" +
            "                <img class=\"card-img-top img-fluid grid-photo\" src=\""+this.imageData+"\" alt=\"\" draggable=\"false\">\n" +
            "                <div class=\"card-body\">\n" +

            "                    <p class=\"card-title h4 small font-weight-bold\">"+this.imageTitle+"</p>\n" +
            "                    <p class=\"card-text h5 small\">"+this.imageContent+"</p>\n" +
            "                </div>\n" +
            "            </div>";
        return ReturnData;

        // var ReturnData = "<div class='btn btn-muted' id='"+this.imageId+"'><img class></div>"
        //
        // return ReturnData;
    }
    getHTML(){
        return this.__cardHTML();
    }
}
// <div id="btn btn-group" >
//     <div class="btn btn-primary " ><i class="fas fa-eraser"></i></div>
// <div class="btn btn-success " ><i class="fas fa-clone "></i></div>
// <div class="btn btn-danger " ><i class="fas fa-heart "></i></div>
// </div>

//var changContent = "";

//onmouseleave=\"threeButton("+this.imageId+")\"
//onmouseenter=\"threeButton("+this.imageId+")\"

//     function threeButton( evt ) {
//
//
// //evt.innerHTML
// //evt.id
//
//         console.log( evt.id  );
//
//
//     }

class Cards{
    constructor(){
        this.cards = [];
        this.folderContentId = "folderContent";// 1721
    }
    addCard( imageData , imageTitle , imageContent , imageId ){
        var card = new Card();
        card.imageContent = imageContent;
        card.imageTitle = imageTitle;
        card.imageData = imageData;
        card.imageId = imageId;

        this.cards.push(card);
    }
    clearCards(  ){
        this.cards = [];
    }
    __creatFakeData(){


        this.addCard( "https://ml4141715.github.io/water/cdn/image/Holder.png" , "imageTitle1" , "imageContent1" , "imageId1" );
        // this.addCard( "https://ml4141715.github.io/water/cdn/image/Holder.png" , "imageTitle1" , "imageContent1" , "imageId1" );
        // this.addCard( "https://ml4141715.github.io/water/cdn/image/Holder.png" , "imageTitle1" , "imageContent1" , "imageId1" );
        // this.addCard( "https://ml4141715.github.io/water/cdn/image/Holder.png" , "imageTitle1" , "imageContent1" , "imageId1" );
        // //console.log(this.cards[0].getHTML());
    }
    __cardsHTML(){
        var total = "";
        for( var i = 0 ; i< this.cards.length ; i ++ ){
            total = total+ this.cards[i].getHTML();
            //console.log(this.cards[i].getHTML());
        }
        //console.log(total);
        return total;
    }
    setHTML(){
        // console.log( "this.__cardsHTML()" );
        // console.log( this.__cardsHTML() );
        //console.log( "this.__cardsHTML()" );

        document.getElementById(this.folderContentId).innerHTML = this.__cardsHTML();

        for( var i = 0 ; i< this.cards.length ; i++ ){

            var cardId = this.cards[i].imageId;

            document.getElementById(cardId).onclick = function (evt) {

                var cardId = evt.path[1].id;

                window.open("/equipment/"+cardId , "_blank");

                //console.log(cardId+"is card Id");
                //console.log(evt);
            }

            //console.log( cardId );


        }
        //console.log( "this.__cardsHTML()" );

    }
}


// var cards = new Cards();
// cards.__creatFakeData();
// cards.setHTML();

//________________________ the description below is how to use this js class_______________________

                    //var cards = new Cards();
                    // cards.__creatFakeData();
                    // cards.__creatFakeData();
                    // cards.__creatFakeData();
                    // cards.setHTML();



