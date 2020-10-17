<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\cartCustomer;

class CartCustomerController extends Controller
{
    public function getCartDetailByUserId(request $request){
        $user = User::whereUserId($request->user_id)->first();
        $carts = cartCustomer::whereUserId($user->id)->get();
        $temp1 = array();
        foreach($carts as $detail){
            $detail->nama_makanan = $detail->makanan->nama;
            $collection = collect($detail);
            $collection->forget("makanan");
            array_push($temp1,$collection);
        }
        return json_encode(["result"=>$temp1]);
    }

    public function appendDataToCart(request $request){
        $user = User::whereUserId($request->user_id)->first();
        $cart = new cartCustomer;
        $cart->user_id = $user->id;
        $cart->makanan_id = $request->makanan_id;
        $cart->harga_makanan = $request->harga_makanan;
        $cart->jumlah = $request->jumlah;
        $cart->notes = !empty($request->notes) ? $request->notes : "";    
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
