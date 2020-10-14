<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\pesananMaster;
use App\pesananDetail;
use App\makanan;

class PesananController extends Controller
{
    //
    public function getAllTodayFinishedOrder(request $request){
        
        $pesanans = pesananMaster::where('status_code','=','003')->whereDate('updated_at', '>=', date('Y-m-d',strtotime($request->tanggalDari)))->whereDate('updated_at', '<=', date('Y-m-d',strtotime($request->tanggalSampai)))->orderBy('updated_at', 'DESC')->get();
        foreach($pesanans as $pesanan){    
            $total_harga = 0;
            foreach($pesanan->details as $pesanan_detail){
                $total_harga += $pesanan_detail->jumlah*$pesanan_detail->harga_makanan;
            }
            $pesanan->total_harga = $total_harga;
        }   
        return json_encode(["result"=>$pesanans]);
    }


    public function getAllTodayUnfinishedOrder(){
        $pesanans = pesananMaster::where('status_code','<>','003')->get();
        foreach($pesanans as $pesanan){    
            $total_harga = 0;
            foreach($pesanan->details as $pesanan_detail){
                $total_harga += $pesanan_detail->jumlah*$pesanan_detail->harga_makanan;
            }
            $pesanan->total_harga = $total_harga;
        }   
        return json_encode(["result"=>$pesanans]);
    }

    public function getOrderByRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        $pesanan_details = pesananDetail::wherePesananMasterId($pesanan->pesanan_master_id)->get();
        foreach($pesanan_details as $pesanan_detail){
            $makanan = makanan::whereMakananId($pesanan_detail->makanan_id)->first();
            $pesanan_detail->nama = $makanan->nama;
            $pesanan_detail->harga_jual = $makanan->harga_jual;
            $pesanan_detail->notes = is_null($pesanan_detail->notes) ? "" :$pesanan_detail->notes;
        }
        return json_encode(["result"=>$pesanan_details]);
    }

    public function updateStatusRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        switch($request->status_code){
            case '001' :
                $pesanan->status_code = '001';
                $pesanan->status = 'pesanan dipesan';       
                break;
            case '002' :
                $pesanan->status_code = '002';
                $pesanan->status = 'pesanan dibuat';
                break;
            // case '003' :
            //     $pesanan->status_code = '002';
            //     $pesanan->status = 'pesanan dibuat';
            //     break;
        } 
        $pesanan->save();
        return json_encode([
            'is_error' => '0',"message"=>"Status Pemesanan Berhasil Diubah"]);
    }
    

    public function updateStatusFinishRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        $pesanan->status_code = '003';
        $pesanan->status = 'pesanan selesai';
        $pesanan->save();
        return json_encode([
            'is_error' => '0',
            "message"=>"Berhasil Selesaikan Pesanan"
        ]);
    }
}
