if(window.sessionStorage.getItem("user") != null) {
    document.getElementById("userLogin").style.display="contents";
    document.getElementById("displayLoginLink").style.display="none";

}
else {
    document.getElementById("userLogin").style.display="none";
    document.getElementById("displayLoginLink").style.display="block";
    
}

logout = () => {
    sessionStorage.clear();
    location.reload();
}