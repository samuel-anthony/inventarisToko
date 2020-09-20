<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\jenisMenu;
use App\jenisMenuDetail;
use App\makanan;

class JenisMenuController extends Controller
{
    public function index(){
        return json_encode(["result"=>jenisMenu::all()]);
    }

    public function jenisMenuDetailByid(request $request){
        $jenisMenu = jenisMenu::find($request->jenis_menu_id);
        $temp = array();
        $temp2 = array();
        foreach($jenisMenu->jenisMenuDetails as $detail){
            array_push($temp, $detail->makanan);
            array_push($temp2, $detail->makanan->makanan_id);
        }
        $jenisMenu->makanan = $temp;
        return json_encode(["result"=>$jenisMenu,"listMakanan"=>makanan::whereNotIn('makanan_id',$temp2)]);
    }

    public function addJenisMenu(request $request){
        $jenisMenu = new jenisMenu;
        $jenisMenu->nama = $request->nama;
        $jenisMenu->save();
        $jenisMenuDetail = json_decode($request->makanans);
        foreach($jenisMenuDetail as $detail){
            $jenisMenuDetail = new jenisMenuDetail;
            $jenisMenuDetail->jenis_menu_id = $jenisMenu->jenis_menu_id;
            $jenisMenuDetail->makanan_id = $jenisMenu->makanan_id;
            $jenisMenuDetail->save();
        }
        
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }

    
    public function updateJenisMenu(request $request){
        $jenisMenu = jenisMenu::find($request->jenis_menu_id);
        $jenisMenu->nama = $request->nama;
        $jenisMenu->save();
        $jenisMenuDetail = json_decode($request->makanans);
        $usedJenisMakananDetail = array();
        foreach($jenisMenuDetail as $detail){
            $jenisMenuDetail = jenisMenuDetail::whereMakananId($detail->makanan_id)->whereJenisMenu($request->jenis_menu_id)->first();
            if(is_null($jenisMenuDetail))
                $jenisMenuDetail = new jenisMenuDetail;
            $jenisMenuDetail->jenis_menu_id = $jenisMenu->jenis_menu_id;
            $jenisMenuDetail->makanan_id = $jenisMenu->makanan_id;
            $jenisMenuDetail->save();     
            array_push($usedJenisMakananDetail,$jenisMenuDetail->jenis_menu_detail_id);
        }
        
        $jenisMenuDetails = jenisMenuDetail::whereJenisMenuId($request->jenis_menu_id)->whereNotIn('jenis_menu_detail_id',$usedJenisMakananDetail)->get();
        foreach($jenisMenuDetails as $detail){
            $detail->delete();
        }
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    } 
    
    
    public function deleteJenisMenu(request $request){
        $jenisMenu = jenisMenu::find($request->jenis_menu_id);
        foreach($jenisMenu->jenisMenuDetails as $detail){
            $detail->delete();
        }
        $jenisMenu->delete();
        return json_encode([
            'is_error' => '0',
            'message' => 'berhasil'
        ]);
    }
}
