function trm_redirect(trm) {
    if (!(trm.substr(0,4) == "http")) {
        trm = "http://" + trm;
    }
    window.location = trm;
}

function trm_delay(trm) {
    var t = setTimeout(trm_redirect(trm), 3000)
}