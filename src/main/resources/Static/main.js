let pepperoniBox = document.getElementById("Pepperoni");
let pepperBox = document.getElementById("Green Pepper");
let tomatoBox = document.getElementById("Tomatoes");
let oliveBox = document.getElementById("Olives");
let baconBox = document.getElementById("Bacon")

let pepperoni = document.getElementById("pepperoniOverlay");
let pepper = document.getElementById("pepperOverlay");
let tomato = document.getElementById("tomatoOverlay");
let olive = document.getElementById("oliveOverlay");
let bacon = document.getElementById("baconOverlay");

function pizzaToppings(){
    if(pepperoniBox.checked === true){
        pepperoni.style.display = "block";
    }else{
        pepperoni.style.display ="none";
    }

    if(pepperBox.checked === true){
        pepper.style.display = "block";
    }else{
        pepper.style.display ="none";
    }

    if(tomatoBox.checked === true){
        tomato.style.display = "block";
    }else{
        tomato.style.display ="none";
    }

    if(oliveBox.checked === true){
        olive.style.display = "block";
    }else{
        olive.style.display ="none";
    }

    if(baconBox.checked === true){
        bacon.style.display = "block";
    }else{
        bacon.style.display ="none";
    }
}

document.getElementById("small").setAttribute("checked", "checked");