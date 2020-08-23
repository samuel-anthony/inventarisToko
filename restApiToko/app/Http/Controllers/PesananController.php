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

    public function updateStatusRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        switch($pesanan->status_code){
            case '001' :
                $pesanan->status_code = '002';
                $pesanan->status = 'pesanan dibuat';       
                break;
            case '002' :
                $pesanan->status_code = '003';
                $pesanan->status = 'pesanan diterima';
                break;
            case '003' :
                $pesanan->status_code = '004';
                $pesanan->status = 'pesanan selesai';
                break;            
            // case '004' :
            //     $pesanan->status_code = '002';
            //     $pesanan->status = 'pesanan dibuat';
            //     break;
        } 
        $pesanan->save();
        return json_encode(["message"=>"status selesai diubah"]);
    }
    

    public function updateStatusFinishRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        $pesanan->status_code = '004';
        $pesanan->status = 'pesanan selesai';
        $pesanan->save();
        return json_encode(["message"=>"status selesai diubah"]);
    }
}
