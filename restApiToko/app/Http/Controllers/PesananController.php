<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\pesananMaster;
use App\pesananDetail;
use App\makanan;
use App\cartCustomer;

class PesananController extends Controller
{
    //
    public function __construct()
    {
        date_default_timezone_set('Asia/Jakarta');
    }
    
    public function getAllTodayFinishedOrder(request $request){
        
        $pesanans = pesananMaster::where('status_code','=','004')->whereDate('updated_at', '>=', date('Y-m-d',strtotime($request->tanggalDari)))->whereDate('updated_at', '<=', date('Y-m-d',strtotime($request->tanggalSampai)))->orderBy('updated_at', 'DESC')->get();
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
        $pesanans = pesananMaster::where('status_code','<>','004')->get();
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
            case '003' :
                $pesanan->status_code = '003';
                $pesanan->status = 'pesanan selesai';
                break;
        } 
        $pesanan->save();
        return json_encode([
            'is_error' => '0',"message"=>"Status Pemesanan Berhasil Diubah"]);
    }
    

    public function updateStatusFinishRefNo(request $request){
        $pesanan = pesananMaster::whereRefNo($request->ref_no)->first();
        $pesanan->status_code = '004';
        $pesanan->status = 'pesanan sudah dibayar';
        $pesanan->save();
        return json_encode([
            'is_error' => '0',
            "message"=>"Berhasil Selesaikan Pesanan"
        ]);
    }

    public function addPesananBaru(request $request){
        $carts = cartCustomer::whereUserId($request->user_id)->get();
        $user = User::whereUserId($request->user_id)->first();
        $refno = date("Y").date("m").date("d");
        $pesananMaster = new pesananMaster;
        $countPesananMasterToday = count(pesananMaster::where('ref_no','LIKE',$refno.'%')->get())+1;
        switch(strlen($countPesananMasterToday)){
            case 1:
                $pesananMaster->ref_no = $refno.'000'.$countPesananMasterToday;
                break;
            case 2:
                $pesananMaster->ref_no = $refno.'00'.$countPesananMasterToday;
                break;
            case 3:
                $pesananMaster->ref_no = $refno.'0'.$countPesananMasterToday;
                break;
            case 4:
                $pesananMaster->ref_no = $refno.$countPesananMasterToday;
                break;
        }
        $pesananMaster->status_code = '001';
        $pesananMaster->status = 'pesanan dipesan';
        $pesananMaster->total_harga = $request->total_harga;
        $pesananMaster->total_charges = 0;
        $pesananMaster->total_pesanan = count($carts);
        $pesananMaster->user_id = $user->id;
        $pesananMaster->save();
        foreach($carts as $detail){
            $pesananDetail = new pesananDetail;
            $pesananDetail->pesanan_master_id = $pesananMaster->id;
            $pesananDetail->makanan_id = $detail->makanan_id;
            $pesananDetail->harga_makanan = $detail->harga_makanan;
            $pesananDetail->jumlah = $detail->jumlah;
            $pesananDetail->notes = $detail->notes;
            $detail->delete();
            $pesananDetail->save();
        }
    }
}
