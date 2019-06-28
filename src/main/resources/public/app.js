GLOB = {
	basketId : null,

	init : function() {
		"use strict";

		document.getElementById("btn-empty").onclick = function() {
			GLOB.deleteBasket(GLOB.basketId);
		};

		document.getElementById("btn-add-1").onclick = function() {
			GLOB.addProductToBasket(GLOB.basketId, "VOUCHER");
		};

		document.getElementById("btn-add-2").onclick = function() {
			GLOB.addProductToBasket(GLOB.basketId, "TSHIRT");
		};

		document.getElementById("btn-add-3").onclick = function() {
			GLOB.addProductToBasket(GLOB.basketId, "MUG");
		};

		GLOB.createNewBasket();
	},

	createNewBasket : function() {
		let ajax = new XMLHttpRequest();

		ajax.open("POST", "/api/v1/basket/", true);
		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

		ajax.onreadystatechange = function() {
			if (this.readyState == 4) {
				if (this.status == 201) {
					GLOB.basketId = this.responseText;
					document.getElementById("total-amount").innerHTML = "0 €";
				}
				else {
					GLOB.createNewBasket();
				}
			}
		};

		ajax.send();
	},

	addProductToBasket : function(id, productCode) {
		let ajax = new XMLHttpRequest();

		ajax.open("PUT", "/api/v1/basket/" + id, true);
		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

		ajax.onreadystatechange = function() {
			if (this.readyState == 4) {
				if (this.status == 204)
					GLOB.refreshTotalAmount(id);
				else
					GLOB.createNewBasket();
			}
		};

		ajax.send(productCode);
	},

	refreshTotalAmount : function(id) {
		let ajax = new XMLHttpRequest();

		ajax.open("GET", "/api/v1/basket/" + id + "/totalamount", true);
		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

		ajax.onreadystatechange = function() {
			if (this.readyState == 4) {
				if (this.status == 200)
					document.getElementById("total-amount").innerHTML = parseFloat(this.responseText).toFixed(2) + " €";
				else
					GLOB.createNewBasket();
			}
		};

		ajax.send();
	},

	deleteBasket : function(id) {
		let ajax = new XMLHttpRequest();

		ajax.open("DELETE", "/api/v1/basket/" + id, true);
		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

		ajax.onreadystatechange = function() {
			if (this.readyState == 4)
				GLOB.createNewBasket();
		};

		ajax.send();
	},
};

GLOB.init();
