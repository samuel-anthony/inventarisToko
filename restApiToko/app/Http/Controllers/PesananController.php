<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\pesananMaster;
use App\pesananDetail;
use App\makanan;

class PesananController extends Controller
{
    //
    public function getAllTodayUnfinishedOrder(){
        $pesanans = pesananMaster::all();
        // foreach($pesanans as $pesanan){
        //     $listDetails =  array();
        //     $pesanan_details = pesananDetail::wherePesananMasterId($pesanan->pesanan_master_id)->get();
        //     foreach($pesanan_details as $pesanan_detail){
        //         $pesanan_detail->makanan = makanan::whereMakananId($pesanan_detail->makanan_id)->first();
        //         array_push($listDetails,$pesanan_detail);
        //     }
        //     $pesanan->details = json_decode(json_encode($listDetails));
        // }
        return json_encode(["result"=>$pesanans]);
    }

    public function getOrderByRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        $pesanan_details = pesananDetail::wherePesananMasterId($pesanan->pesanan_master_id)->get();
        foreach($pesanan_details as $pesanan_detail){
            $makanan = makanan::whereMakananId($pesanan_detail->makanan_id)->first();
            $pesanan_detail->nama = $makanan->nama;
            $pesanan_detail->harga_jual = $makanan->harga_jual;
        }
        return json_encode(["result"=>$pesanan_details]);
    }
}
