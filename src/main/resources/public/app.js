GLOB = {
	basketId: null,
	
	init: function() {
	    "use strict";

	    $("#btn-empty").on("click", function() {
	    	GLOB.deleteBasket(GLOB.basketId);
	    });

	    $("#btn-add-1").on("click", function() {
	    	GLOB.addProductToBasket(GLOB.basketId, "VOUCHER");
	    });

	    $("#btn-add-2").on("click", function() {
	    	GLOB.addProductToBasket(GLOB.basketId, "TSHIRT");
	    });

	    $("#btn-add-3").on("click", function() {
	    	GLOB.addProductToBasket(GLOB.basketId, "MUG");
	    });
	    
	    GLOB.createNewBasket();
	},
	
	createNewBasket: function() {
	    $.ajax({
	        type: "POST",
	        url: "/api/v1/basket",
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
	        success: function (response) {
	        	GLOB.basketId = response;
	        	$("#total-amount").html("0 €")
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            console.log(jqXHR.status);
	        }
	    });
	},
	
	addProductToBasket: function(id, productCode) {
	    $.ajax({
	        type: "PUT",
            url: "/api/v1/basket/" + id,
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
            data: productCode,
	        success: function (response) {
            	GLOB.refreshTotalAmount(id);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            console.log(jqXHR.status);
	        }
	    });
	},
	
	refreshTotalAmount: function(id) {
	    $.ajax({
	        type: "GET",
            url: "/api/v1/basket/" + id + "/totalamount",
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
	        success: function (response) {
	        	$("#total-amount").html(parseFloat(response).toFixed(2) + " €")
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            console.log(jqXHR.status);
	        }
	    });
	},
	
	deleteBasket: function(id) {
        $.ajax({
            type: "DELETE",
            url: "/api/v1/basket/" + id,
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
            dataType: "json",
            success: function (responseJson) {
            	GLOB.createNewBasket();
            },
            error: function(jqXHR, textStatus, errorThrown) {
	            console.log(jqXHR.status);
            }
        });
	},
};

$(document).ready(function() {
    "use strict";
    GLOB.init();
});
