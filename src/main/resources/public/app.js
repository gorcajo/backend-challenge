//GLOB = {
//	basketId : null,
//
//	init : function() {
//		"use strict";
//
//		document.getElementById("btn-empty").onclick = function() {
//			GLOB.deleteBasket(GLOB.basketId);
//		};
//
//		document.getElementById("btn-add-1").onclick = function() {
//			GLOB.addProductToBasket(GLOB.basketId, "VOUCHER");
//		};
//
//		document.getElementById("btn-add-2").onclick = function() {
//			GLOB.addProductToBasket(GLOB.basketId, "TSHIRT");
//		};
//
//		document.getElementById("btn-add-3").onclick = function() {
//			GLOB.addProductToBasket(GLOB.basketId, "MUG");
//		};
//
//		GLOB.createNewBasket();
//	},
//
//	createNewBasket : function() {
//		let ajax = new XMLHttpRequest();
//
//		ajax.open("POST", "/api/v1/basket/", true);
//		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//
//		ajax.onreadystatechange = function() {
//			if (this.readyState === 4) {
//				if (this.status === 201) {
//					GLOB.basketId = this.responseText;
//					document.getElementById("total-amount").innerHTML = "0 €";
//				}
//				else {
//					alert("HTTP " + this.status);
//				}
//			}
//		};
//
//		ajax.send();
//	},
//
//	addProductToBasket : function(id, productCode) {
//		let ajax = new XMLHttpRequest();
//
//		ajax.open("PUT", "/api/v1/basket/" + id, true);
//		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//
//		ajax.onreadystatechange = function() {
//			if (this.readyState === 4) {
//				if (this.status === 204)
//					GLOB.refreshTotalAmount(id);
//				else
//					GLOB.createNewBasket();
//			}
//		};
//
//		ajax.send(productCode);
//	},
//
//	refreshTotalAmount : function(id) {
//		let ajax = new XMLHttpRequest();
//
//		ajax.open("GET", "/api/v1/basket/" + id + "/totalamount", true);
//		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//
//		ajax.onreadystatechange = function() {
//			if (this.readyState === 4) {
//				if (this.status === 200)
//					document.getElementById("total-amount").innerHTML = parseFloat(this.responseText).toFixed(2) + " €";
//				else
//					GLOB.createNewBasket();
//			}
//		};
//
//		ajax.send();
//	},
//
//	deleteBasket : function(id) {
//		let ajax = new XMLHttpRequest();
//
//		ajax.open("DELETE", "/api/v1/basket/" + id, true);
//		ajax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
//
//		ajax.onreadystatechange = function() {
//			if (this.readyState === 4)
//				GLOB.createNewBasket();
//		};
//
//		ajax.send();
//	},
//};
//
//GLOB.init();

class Products extends React.Component {

    render() {
        return (
            <div className='col-md-8'>
                <div className='card'>
                    <div className='card-header bg-primary text-white section-header'>Products</div>
                    
                    <div className='card-body'>
                        <div className='row'>
                            <div className='col-md-4'>
                                <div className='card'>
                                    <div className='card-header product-header'>Cabify Voucher</div>
                                    <div className='card-body'>
                                        <p className='product-amount'>5.00 €</p>
                                    <button id='btn-add-1' type='button' className='btn btn-success btn-block'>Add to basket</button>
                                    </div>
                                </div>
                            </div>
                            <div className='col-md-4'>
                                <div className='card'>
                                    <div className='card-header product-header'>Cabify T-Shirt</div>
                                    <div className='card-body'>
                                        <p className='product-amount'>20.00 €</p>
                                        <button id='btn-add-2' type='button' className='btn btn-success btn-block'>Add to basket</button>
                                    </div>
                                </div>
                            </div>
                            <div className='col-md-4'>
                                <div className='card'>
                                    <div className='card-header product-header'>Cabify Coffee Mug</div>
                                    <div className='card-body'>
                                        <p className='product-amount'>7.50 €</p>
                                        <button id='btn-add-3' type='button' className='btn btn-success btn-block'>Add to basket</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

class Basket extends React.Component {

    render() {
        return (
            <div className='col-md-3 offset-md-1'>
                <div className='card'>
                    <div className='card-header bg-primary text-white section-header'>Basket</div>
                    
                    <div className='card-body'>
                        <div className='row'>
                            <div className='col-md-12'>
                                <p id='total-amount' className='basket-amount'>0 €</p>
                            </div>
                        </div>
                        
                        <div className='row'>
                            <div className='col-md-12'>
                                <button id='btn-empty' type='button' className='btn btn-danger btn-block'>Empty basket</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

// ========================================

ReactDOM.render(
    <div>
	    <div className='row'>
		    <div className='col-md-12'>
		        <div className='header'>
		            <h1>Backend Challenge</h1>
		            <h4><i>Guillermo Orcajo García</i></h4>
		        </div>
		    </div>
		</div>
		
	    <div className='row'>
	        <Products />
	        <Basket />
	    </div>
    </div>,
    document.getElementById('react-root')
);

