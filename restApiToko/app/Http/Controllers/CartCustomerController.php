<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\cartCustomer;

class CartCustomerController extends Controller
{
    public function getCartDetailByUserId(request $request){
        $carts = cartCustomer::whereUserId($request->user_id)->get();
        foreach($carts as $detail){
            $cart->makanan();
        }
        return json_encode(["result"=>$carts]);
    }

    public function appendDataToCart(request $request){
        $user = User::whereUserId($request->user_id)->first();
        $cart = new cartCustomer;
        $cart->user_id = $user->id;
        $cart->makanan_id = $request->makanan_id;
        $cart->harga_makanan = $request->harga_makanan;
        $cart->jumlah = $request->jumlah;
        $cart->notes = $request->notes;    
        $cart->save();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    public function clearCart(request $request){
        $carts = cartCustomer::whereUserId($request->user_id)->get();
        foreach($carts as $detail){
            $detail->delete();
        }
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }
}
